package com.lego.survey.config;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * @author yanglf
 * @description
 * @since 2019/1/24
 **/
public class CustomerDoubleSerialize extends JsonSerializer<Double> {

    @Override
    public void serialize(Double aDouble, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (aDouble != null) {
            if (aDouble > 0) {
                jsonGenerator.writeString(aDouble + "");
            } else {
                jsonGenerator.writeString(formatFloatNumber(aDouble));
            }
        }
    }

    private static String formatFloatNumber(double value) {
        if (value != 0.00) {
            DecimalFormat decimalFormat = new DecimalFormat("##0.00000");
            return decimalFormat.format(value);
        } else {
            return "0.00";
        }
    }


}
