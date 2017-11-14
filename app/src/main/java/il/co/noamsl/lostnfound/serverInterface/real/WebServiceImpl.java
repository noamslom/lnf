package il.co.noamsl.lostnfound.serverInterface.real;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import java.util.List;

import il.co.noamsl.lostnfound.dataTransfer.Request;
import il.co.noamsl.lostnfound.eitan.LostTable;
import il.co.noamsl.lostnfound.eitan.LostTableList;
import il.co.noamsl.lostnfound.eitan.ServerAPI;
import il.co.noamsl.lostnfound.item.FakeItem;
import il.co.noamsl.lostnfound.dataTransfer.RequestAgent;
import il.co.noamsl.lostnfound.dataTransfer.ItemReceiver;
import il.co.noamsl.lostnfound.item.LFItem;
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
    private static final ServerAPI API = new Retrofit.Builder()
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
    public void requestItems(final Request<LFItem> request, RequestAgent requestAgent) {
        if (requestAgent != null) {
            throw new UnsupportedOperationException("Not imp yet");
        }
        API.lost_queryItems("wallet", null, null).enqueue(new Callback<LostTableList>() {
            @Override
            public void onResponse(Call<LostTableList> call, Response<LostTableList> response) {
                if (response.isSuccessful()) {
                    List<LostTable> lst = response.body().getLostTables();
                    for (LostTable l : lst) {
                        System.out.println(l.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<LostTableList> call, Throwable t) {
                //// FIXME: 13/11/2017 mock
                if(request.getDataPosition().getLast()!=null){ //// FIXME: 14/11/2017 server should take care of this
                    return;
                }
                for (int i = 0; i < 1000; i++) {
                    request.getItemReceiver().onItemArrived(new FakeItem(i,"wal"+i,"descrip"+i,null,null,new FakeImage()));
                }
            }
        });


    }

    @Override
    public FakeItem getItemById(long itemId) {
        return null;
    }

    @Override
    public void addItem(String text) {

    }
}
