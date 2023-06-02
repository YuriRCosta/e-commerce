package br.com.ecommerce.ecommerce;

import br.com.ecommerce.ecommerce.enums.ApiTokenIntegracao;
import okhttp3.*;

import java.io.IOException;

public class TestAPIMelhorEnvio {

    public static void main(String[] args) throws IOException {

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"orders\":[\"9709069b-8e6f-482e-aca7-fee4eae2a036\"],\"mode\":\"private\"}");
        Request request = new Request.Builder()
                .url("https://sandbox.melhorenvio.com.br/api/v2/me/shipment/print")
                .post(body)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO)
                .addHeader("User-Agent", "n0xfps1@gmail.com")
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response.body().string());

//        OkHttpClient client = new OkHttpClient();
//
//        MediaType mediaType = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(mediaType, "{\"orders\":[\"9709069b-8e6f-482e-aca7-fee4eae2a036\"]}");
//        Request request = new Request.Builder()
//                .url("https://sandbox.melhorenvio.com.br/api/v2/me/shipment/generate")
//                .post(body)
//                .addHeader("Accept", "application/json")
//                .addHeader("Content-Type", "application/json")
//                .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO)
//                .addHeader("User-Agent", "n0xfps1@gmail.com")
//                .build();
//
//        Response response = client.newCall(request).execute();
//
//        System.out.println(response.body().string());

//        OkHttpClient client = new OkHttpClient();
//
//        MediaType mediaType = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(mediaType, "{\"orders\":[\"9709069b-8e6f-482e-aca7-fee4eae2a036\"]}");
//        Request request = new Request.Builder()
//                .url("https://sandbox.melhorenvio.com.br/api/v2/me/shipment/checkout")
//                .post(body)
//                .addHeader("Accept", "application/json")
//                .addHeader("Content-Type", "application/json")
//                .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO)
//                .addHeader("User-Agent", "ReadMe-API-Explorer")
//                .build();
//
//        Response response = client.newCall(request).execute();
//
//        System.out.println(response.body().string());

//        OkHttpClient client = new OkHttpClient().newBuilder().build();
//        MediaType mediaType = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(mediaType, "{\"from\":{\"name\":\"Yuri\",\"phone\":\"51985280225\",\"email\":\"yuriramoscosta@hotmail.com\",\"document\":\"04584572054\",\"address\":\"Rua Bomfim\",\"complement\":\"casa\",\"number\":\"179\",\"district\":\"Santa Luzia\",\"city\":\"Capão da Canoa\",\"country_id\":\"BR\",\"postal_code\":\"95555-000\",\"state_abbr\":\"RS\"},\"to\":{\"name\":\"Yasmin\",\"phone\":\"5198901289\",\"email\":\"yasmin@gmail.com\",\"document\":\"114.185.270-50\",\"address\":\"Rua Seila\",\"complement\":\"casa\",\"number\":\"123\",\"district\":\"Santa Luzia\",\"city\":\"Capão da Canoa\",\"country_id\":\"BR\",\"postal_code\":\"95555-000\",\"state_abbr\":\"RS\"},\"products\":[{\"name\":\"Iphone 13\",\"quantity\":\"2\",\"unitary_value\":\"1200\"}],\"volumes\":{\"height\":20,\"width\":10,\"length\":30,\"weight\":1.2},\"options\":{\"invoice\":{\"key\":\"31190307586261000184550010000092481404848162\"},\"insurance_value\":1200,\"receipt\":false,\"own_hand\":false,\"reverse\":false,\"non_commercial\":false},\"service\":1,\"agency\":39}");
//
//        Request request = new Request.Builder()
//                .url("https://sandbox.melhorenvio.com.br/api/v2/me/cart")
//                .post(body)
//                .addHeader("Accept", "application/json")
//                .addHeader("Content-Type", "application/json")
//                .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO)
//                .addHeader("User-Agent", "n0xfps1@gmail.com")
//                .build();
//
//        Response response = client.newCall(request).execute();
//
//        System.out.println(response.body().string());
    }



}
