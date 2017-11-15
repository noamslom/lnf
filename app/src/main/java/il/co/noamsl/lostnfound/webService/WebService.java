package il.co.noamsl.lostnfound.webService;

import android.util.Log;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import java.util.List;
import java.util.Random;

import il.co.noamsl.lostnfound.webService.dataTransfer.ItemQuery;
import il.co.noamsl.lostnfound.webService.dataTransfer.Request;
import il.co.noamsl.lostnfound.webService.eitan.LostTable;
import il.co.noamsl.lostnfound.webService.eitan.LostTableList;
import il.co.noamsl.lostnfound.webService.eitan.ServerAPI;
import il.co.noamsl.lostnfound.repository.item.LfItem;
import il.co.noamsl.lostnfound.webService.dataTransfer.RequestAgent;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by noams on 13/11/2017.
 */

public class WebService {

    // '/' at the end is required
    private static final String BASE_URL = "http://10.0.2.2:8080/lf_server/webresources/";

    // order at which converters are added matters!
    // Scalar converter should be added first.
    //// FIXME: 15/11/2017 change to private
    public static final ServerAPI API = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(new OkHttpClient())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(
                    SimpleXmlConverterFactory.createNonStrict(
                            new Persister(new AnnotationStrategy())
                    )
            )
            .build().create(ServerAPI.class);


    public void requestItems(final Request<LfItem> request, RequestAgent requestAgent, ItemQuery query) {
        if (requestAgent != null) {
            throw new UnsupportedOperationException("Not imp yet");
        }
        if(request.getDataPosition().getLast()!=null){ //// FIXME: 14/11/2017 server should take care of this
           request.getItemReceiver().onItemArrived(null);
        }

        if (query.isAFound()) {
            //TODO
        }
        else{
            API.lost_queryItems("wal", null, null).enqueue(new Callback<LostTableList>() {
                @Override
                public void onResponse(Call<LostTableList> call, Response<LostTableList> response) {
                    Log.d("serverd", "responded"+ response.toString());
                    if (response.isSuccessful()) {
                        if(request.getDataPosition().getLast()!=null){ //// FIXME: 14/11/2017 server should take care of this
                            return;
                        }

                        List<LostTable> lst = response.body().getLostTables();

                        if(lst==null)
                            return;
                        for (LostTable l : lst) {
                            Log.d("serverd",l.toString());
                            request.getItemReceiver().onItemArrived(new LfItem(l));
                        }
                        request.getItemReceiver().onItemArrived(null);

                    }
                }

                @Override
                public void onFailure(Call<LostTableList> call, Throwable t) {
                    //// FIXME: 13/11/2017 mock

                    if(request.getDataPosition().getLast()!=null){ //// FIXME: 14/11/2017 server should take care of this
                        return;
                    }
                    for (int i = 0; i < 100; i++) {

                        request.getItemReceiver().onItemArrived(
                                new LfItem(i, "wal" + i, "descrip" + i, null, null, null, new Random().nextBoolean(), true));
                    }
                    request.getItemReceiver().onItemArrived(null);
                }
            });

        }

    }

    public void addItem(LfItem lfItem) {
        if(lfItem.isAFound()) {
            Call<Integer> integerCall = API.found_create(lfItem.toFoundTable());
            Log.d("serverd", "found created call: "+integerCall+" Item "+lfItem);

        }
        else if(lfItem.isALost()){
            Log.d("serverd", "lost create");

            API.lost_create(lfItem.toLostTable()).enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    Log.d("serverd","message"+response.message()+"body"+response.raw());
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Log.d("serverd","create failed",t);
                }
            });
        }
        else{
            throw new RuntimeException("LfItem must be a lost or a found");
        }
    }

    public void updateItem(LfItem lfItem){
        if(lfItem.isALost()) {
            API.lost_edit(lfItem.toLostTable()).enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    Log.d("serverd", "onResponse" + response.raw() + "");
                    Log.d("serverd", "onResponse" + response.body() + "");
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {

                }
            });
        }
        if(lfItem.isAFound()){
            API.found_edit(lfItem.toFoundTable()).enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    Log.d("serverd", "onResponse" + response.raw() + "");
                    Log.d("serverd", "onResponse" + response.body() + "");

                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {

                }
            });
        }
    }

}
