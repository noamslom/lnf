package il.co.noamsl.lostnfound.serverInterface;

import android.util.Log;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import java.util.List;
import java.util.Random;

import il.co.noamsl.lostnfound.dataTransfer.Request;
import il.co.noamsl.lostnfound.eitan.LostTable;
import il.co.noamsl.lostnfound.eitan.LostTableList;
import il.co.noamsl.lostnfound.eitan.ServerAPI;
import il.co.noamsl.lostnfound.item.LfItemImpl;
import il.co.noamsl.lostnfound.dataTransfer.RequestAgent;
import il.co.noamsl.lostnfound.item.LfItem;
import il.co.noamsl.lostnfound.serverInterface.WebService;
import il.co.noamsl.lostnfound.serverInterface.fake.FakeImage;
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

public class WebServiceImpl implements WebService {

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

    @Override
    public void requestItems(final Request<LfItem> request, RequestAgent requestAgent) {
        if (requestAgent != null) {
            throw new UnsupportedOperationException("Not imp yet");
        }
        API.lost_queryItems("wallet", null, null).enqueue(new Callback<LostTableList>() {
            @Override
            public void onResponse(Call<LostTableList> call, Response<LostTableList> response) {
                if (response.isSuccessful()) {
                    List<LostTable> lst = response.body().getLostTables();
                    if(lst==null)
                        return;
                    for (LostTable l : lst) {
                        Log.d("serverd",l.toString());
                    }
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
                            new LfItemImpl(i, "wal" + i, "descrip" + i, null, null, null, new Random().nextBoolean(), true));
                }
            }
        });


    }

    @Override
    public LfItemImpl getItemById(long itemId) {
        return null;
    }

    @Override
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
}
