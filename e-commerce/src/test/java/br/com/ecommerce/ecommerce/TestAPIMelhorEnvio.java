package br.com.ecommerce.ecommerce;

import br.com.ecommerce.ecommerce.enums.ApiTokenIntegracao;
import okhttp3.*;

import java.io.IOException;

public class TestAPIMelhorEnvio {

    public static void main(String[] args) throws IOException {

        OkHttpClient client = new OkHttpClient().newBuilder().build();

        MediaType mediaType = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(mediaType, "{ \"from\": { \"postal_code\": \"96020360\" }, \"to\": { \"postal_code\": \"01018020\" }, \"products\": [ { \"id\": \"x\", \"width\": 11, \"height\": 17, \"length\": 11, \"weight\": 0.3, \"insurance_value\": 10.1, \"quantity\": 1 } ], \"options\": { \"receipt\": false, \"own_hand\": false }, \"services\": \"1,2,18\" }");

        Request request = new Request.Builder()

                .url("https://sandbox.melhorenvio.com.br/api/v2/me/shipment/calculate")
                .post(body)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO)
                .addHeader("User-Agent", "n0xfps1@gmail.com")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());

    }

}
