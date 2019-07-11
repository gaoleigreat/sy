package com.lego.survey.monitor.config;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.*;
import de.codecentric.boot.admin.server.notify.AbstractStatusChangeNotifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yanglf
 * @description
 * @since 2019/1/28
 **/
@Slf4j
public class DingTalkNotifier extends AbstractStatusChangeNotifier {

    private static final String DEFAULT_MESSAGE = "*#{instance.registration.name}* (#{instance.registration.serviceUrl}) is *#{instance.statusInfo.status}**  #{timestamp} ";

    private final SpelExpressionParser parser = new SpelExpressionParser();
    private RestTemplate restTemplate = new RestTemplate();
    private String webhookToken;
    private boolean isAtAll;
    private String atMobiles;
    private String msgType = "markdown";
    private String title = "服务告警";
    private Expression message;

    private String[] ignoreChanges = {"UNKNOWN:UP"};

    public DingTalkNotifier(InstanceRepository repository) {
        super(repository);
        this.message = parser.parseExpression(DEFAULT_MESSAGE, ParserContext.TEMPLATE_EXPRESSION);
    }


    @Override
    protected Mono<Void> doNotify(InstanceEvent event,Instance instance) {
        log.info("service status event:{}, instance:{}", event, instance);
        return Mono.fromRunnable(() -> restTemplate.postForEntity(webhookToken, createMessage(event, instance), Void.class));
    }

  /*  @Override
    public Mono<Void> notify(@Nullable InstanceEvent event) {
        log.info("InstanceEvent-----------{}",event);
        Mono<Instance> instanceMono = repository.find(event.getInstance());
        return instanceMono.filter(instance -> {
            log.info("shouldNotify------------------");
            return this.shouldNotify(event, instance);
        }).flatMap(instance -> {
            log.info("doNotify--------------------------------------");
            return this.doNotify(event, instance);
        }).doOnError((ex) -> {
            log.info("Couldn't notify for event {} ", event, ex);
        }).then();
    }*/

    private HttpEntity<Map<String, Object>> createMessage(InstanceEvent event, Instance instance) {
        Map<String, Object> messageJson = new HashMap<>(16);
        HashMap<String, String> params = new HashMap<>(16);
        params.put("text", this.getMessage(event, instance));
        params.put("title", this.title);
        messageJson.put("atMobiles", this.atMobiles);
        //文本（text）、连接（link）、markdown（markdown）
        messageJson.put("msgtype", this.msgType);
        messageJson.put(this.msgType, params);
        messageJson.put("isAtAll", this.isAtAll);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new HttpEntity<>(messageJson, headers);
    }


    private String getMessage(InstanceEvent event, Instance instance) {
        Map<String, Object> root = new HashMap<>(16);
        root.put("event", event);
        root.put("instance", instance);
        root.put("status", event.getType());
        root.put("timestamp", event.getTimestamp().atZone(ZoneId.systemDefault()).toString());
        root.put("lastStatus", getLastStatus(event.getInstance()));
        StandardEvaluationContext context = new StandardEvaluationContext(root);
        context.addPropertyAccessor(new MapAccessor());
        return message.getValue(context, String.class);
    }


    private String getAtMobilesString(String s) {
        StringBuilder atMobiles = new StringBuilder();
        String[] mobiles = s.split(",");
        for (String mobile : mobiles) {
            atMobiles.append("@").append(mobile);
        }
        return atMobiles.toString();
    }


    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getWebhookToken() {
        return webhookToken;
    }

    public void setWebhookToken(String webhookToken) {
        this.webhookToken = webhookToken;
    }

    public String getAtMobiles() {
        return atMobiles;
    }

    public void setAtMobiles(String atMobiles) {
        this.atMobiles = atMobiles;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public Expression getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = this.parser.parseExpression(message, ParserContext.TEMPLATE_EXPRESSION);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isAtAll() {
        return isAtAll;
    }

    public void setAtAll(boolean atAll) {
        isAtAll = atAll;
    }

    @Override
    protected boolean shouldNotify(InstanceEvent event, Instance instance) {
        String from = getLastStatus(event.getInstance());
        String to = instance.getStatusInfo().getStatus();
        return Arrays.binarySearch(ignoreChanges, from + ":" + to) < 0
                && Arrays.binarySearch(ignoreChanges, "*:" + to) < 0
                && Arrays.binarySearch(ignoreChanges, from + ":*") < 0;
    }

    @Override
    public void setIgnoreChanges(String[] ignoreChanges) {
        String[] copy = Arrays.copyOf(ignoreChanges, ignoreChanges.length);
        Arrays.sort(copy);
        this.ignoreChanges = copy;
    }
}
