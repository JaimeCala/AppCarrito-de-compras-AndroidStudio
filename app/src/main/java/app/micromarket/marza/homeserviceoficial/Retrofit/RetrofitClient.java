package app.micromarket.marza.homeserviceoficial.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private  static Retrofit retrofit= null;

    public static Retrofit getClient(String baseUrl){

        if(retrofit==null)
        {
            Gson gson = new GsonBuilder()
                    //.registerTypeAdapter(Pedido.class,new ImgCategoriaDeserializer())
                    .setLenient()
                    .create(); // para string para un solo elemento

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
