package il.co.noamsl.lostnfound.webService;

import android.util.Log;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import java.util.List;

import il.co.noamsl.lostnfound.repository.User.User;
import il.co.noamsl.lostnfound.webService.dataTransfer.ItemReceiver;
import il.co.noamsl.lostnfound.webService.dataTransfer.ItemsQuery;
import il.co.noamsl.lostnfound.webService.dataTransfer.Request;
import il.co.noamsl.lostnfound.webService.eitan.FoundTable;
import il.co.noamsl.lostnfound.webService.eitan.FoundTableList;
import il.co.noamsl.lostnfound.webService.eitan.LostTable;
import il.co.noamsl.lostnfound.webService.eitan.LostTableList;
import il.co.noamsl.lostnfound.webService.eitan.ServerAPI;
import il.co.noamsl.lostnfound.repository.item.LfItem;
import il.co.noamsl.lostnfound.webService.dataTransfer.RequestAgent;
import il.co.noamsl.lostnfound.webService.eitan.Users;
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
    private static final User FAKE_USER = new User(new Users("N","a@gds.com","050-1234567","Hereeee",777));

    private static final String TAG = "WebService";
    // '/' at the end is required
    private static final String BASE_URL = "http://10.0.2.2:8080/lf_server/webresources/";
    // order at which converters are added matters!
    // Scalar converter should be added first.
    //// FIXME: 15/11/2017 change to private
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


    public void requestItems(final Request<LfItem> request, RequestAgent requestAgent) {
        Log.d(TAG, "requestItems: ");

        if (requestAgent != null) {
            throw new UnsupportedOperationException("Not imp yet");
        }
        if (request.getDataPosition().getLast() != null) { //// FIXME: 14/11/2017 server should take care of this
            request.getItemReceiver().onItemArrived(null);
            return;
        }
        ItemsQuery query = (ItemsQuery) request.getQuery();

        if (query.getOwner() != null) {
            requestItemsOfUser(request);
        } else {
            requestItemsByFilter(request);
        }

    }

    private void requestItemsOfUser(final Request<LfItem> request) {
        //// TODO: 16/11/2017 implement for real
/*
        request.getItemReceiver().onItemArrived(new LfItem(107, "wallet tekjke" + 107, "descrip" + 107, "here", 777, "pic", new Random().nextBoolean(), true));
        request.getItemReceiver().onItemArrived(null);
*/
        Log.d(TAG, "requestItemsOfUser: ");
        ItemsQuery query = (ItemsQuery) request.getQuery();
        Boolean isAFound = query.isAFound();
        Log.d(TAG, "requestItemsOfUser: isAFound = " + isAFound);

        MultipleSourcesItemReceiver<LfItem> msItemReceiver = new MultipleSourcesItemReceiver<>(request.getItemReceiver());

        if (isAFound == null || isAFound) {
            Founds.requestItemsOfUser(request, query.getOwner(), msItemReceiver);
        }
        if (isAFound == null || !isAFound) {
            Losts.requestItemsOfUser(request, query.getOwner(), msItemReceiver);
        }

    }


    private void requestItemsByFilter(final Request<LfItem> request) {
/*
        //// FIXME: 16/11/2017 delete this, just for faster testings
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 20; i++) {

                        request.getItemReceiver().onItemArrived(
                                new LfItem(i, "wallet tekjke" + i, "descrip" + i, "here", 5, "pic", new Random().nextBoolean(), true));
                        Log.d(TAG, "run: received item" + i);

                    }
                    request.getItemReceiver().onItemArrived(null);
                }
            }, 1000);
            if (true) return;
        }
*/
        ItemsQuery query = (ItemsQuery) request.getQuery();
        MultipleSourcesItemReceiver<LfItem> msItemReceiver = new MultipleSourcesItemReceiver<>(request.getItemReceiver());
        Boolean isAFound = query.isAFound();
        if (isAFound == null || isAFound) {
            Founds.requestItemsOfUser(request, query.getOwner(), msItemReceiver);
        }
        if (isAFound == null || !isAFound) {
            Losts.requestItemsOfUser(request, query.getOwner(), msItemReceiver);
        }

        if (query.isAFound()) {
            Founds.requestByFilter(request, msItemReceiver);
        } else {
            Losts.requestByFilter(request, msItemReceiver);
        }

    }

    public void addItem(final ItemReceiver<Boolean> itemReceiver,LfItem lfItem) {
        //// TODO: 17/11/2017 add failure handling
        if (lfItem.isAFound()) {
            Founds.addItem(itemReceiver,lfItem);
        } else if (lfItem.isALost()) {
            Losts.addItem(itemReceiver,lfItem);
        } else {
            throw new RuntimeException("LfItem must be a lost or a found");
        }
    }

    public void updateItem(ItemReceiver<Boolean> itemReceiver, LfItem newItem) {
        itemReceiver.onItemArrived(true);
        if (newItem.isALost()) {
            Losts.updateItem(itemReceiver,newItem);
        }
        if (newItem.isAFound()) {
            Founds.updateItem(itemReceiver,newItem);

        }
    }

    public void getUserById(final ItemReceiver<User> itemReceiver, int userId) {
        Callback<Users> callback = new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if(!response.isSuccessful()){
                    itemReceiver.onRequestFailure();
                    return;
                }
                itemReceiver.onItemArrived(new User(response.body()));
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                itemReceiver.onRequestFailure();
            }
        };
        API.user_getSettings(userId,null).enqueue(callback);
    }

    public void getUserByCredential(final ItemReceiver<User> itemReceiver, String credential) {
        Log.d(TAG, "onResponse: credential = " + credential);

//        itemReceiver.onItemArrived(FAKE_USER);
        //fixme uncomment the code
        Callback<Users> callback = new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {

                Log.d(TAG, "getUserByCredential.onResponse: "+response.isSuccessful()+"Body="+response.body());
                if(!response.isSuccessful()){
                    itemReceiver.onRequestFailure();
                    return;
                }
                if(response.body()==null){
                    itemReceiver.onRequestFailure();
                    return;
                }
                itemReceiver.onItemArrived(new User(response.body()));
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                itemReceiver.onRequestFailure();
            }
        };
        API.user_getSettings(null,credential).enqueue(callback);
    }

    public void updateUser(final ItemReceiver<User> itemReceiver, final User user) {
        Callback<Integer> callback = new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(!response.isSuccessful()){
                    itemReceiver.onRequestFailure();
                    return;
                }
                itemReceiver.onItemArrived(user);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                itemReceiver.onRequestFailure();
            }
        };
        API.user_edit(user.toWSUser()).enqueue(callback);
    }

    private static class Founds {
        private static void requestItemsOfUser(Request<LfItem> request, Integer owner, MultipleSourcesItemReceiver<LfItem> msItemReceiver) {
            Log.d(TAG, "requestItemsOfUser: ");

            final ItemReceiver<LfItem> itemReceiver = msItemReceiver;
            msItemReceiver.onWorkerStarted();
            Callback<FoundTableList> callback = new Callback<FoundTableList>() {
                @Override
                public void onResponse(Call<FoundTableList> call, Response<FoundTableList> response) {
                    if (!response.isSuccessful()) {
                        itemReceiver.onRequestFailure();
                        return;
                    }
                    List<FoundTable> foundTables = response.body().getFoundTables();
                    if(foundTables!=null) {
                        for (FoundTable foundTable : foundTables) {
                            itemReceiver.onItemArrived(new LfItem(foundTable));
                        }
                    }
                    itemReceiver.onItemArrived(null);

                }

                @Override
                public void onFailure(Call<FoundTableList> call, Throwable t) {
                    Log.d(TAG, "onFailure: ");

                    itemReceiver.onRequestFailure();
                }
            };
            API.user_getFoundItems(owner).enqueue(callback);
        }


        private static void requestByFilter(final Request<LfItem> request, final MultipleSourcesItemReceiver<LfItem> msItemReceiver) {
            ItemsQuery query = (ItemsQuery) request.getQuery();
            msItemReceiver.onWorkerStarted();
            Callback<FoundTableList> callback = new Callback<FoundTableList>() {
                @Override
                public void onResponse(Call<FoundTableList> call, Response<FoundTableList> response) {
                    if (!response.isSuccessful()) {
                        msItemReceiver.onRequestFailure();
                        return;
                    }
                    List<FoundTable> lst = response.body().getFoundTables();
                    if(lst!=null){
                        for (FoundTable l : lst) {
                            msItemReceiver.onItemArrived(new LfItem(l));
                        }
                    }
                    msItemReceiver.onItemArrived(null);


                }

                @Override
                public void onFailure(Call<FoundTableList> call, Throwable t) {
                    msItemReceiver.onRequestFailure();
                }
            };
            API.found_queryItems(query.getName(), query.getDescription(), query.getLocation()).enqueue(callback);

        }

        public static void updateItem(final ItemReceiver<Boolean> itemReceiver, LfItem newItem) {
            API.found_edit(newItem.toFoundTable()).enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if(!response.isSuccessful()) {
                        itemReceiver.onRequestFailure();
                        return;
                    }
                    itemReceiver.onItemArrived(true);
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    itemReceiver.onRequestFailure();
                }
            });
        }

        public static void addItem(final ItemReceiver<Boolean> itemReceiver,LfItem lfItem) {
            Callback<Integer> callback = new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if(!response.isSuccessful()){
                        itemReceiver.onRequestFailure();
                        return;
                    }
                    itemReceiver.onItemArrived(true);
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    itemReceiver.onRequestFailure();
                }
            };
            API.found_create(lfItem.toFoundTable()).enqueue(callback);
        }
    }

    private static class Losts {
        private static void requestItemsOfUser(Request<LfItem> request, Integer owner, MultipleSourcesItemReceiver<LfItem> msItemReceiver) {
            Log.d(TAG, "requestItemsOfUser: ");

            final ItemReceiver<LfItem> itemReceiver = msItemReceiver;
            msItemReceiver.onWorkerStarted();
            Callback<LostTableList> callback = new Callback<LostTableList>() {
                @Override
                public void onResponse(Call<LostTableList> call, Response<LostTableList> response) {
                    if (!response.isSuccessful()) {
                        itemReceiver.onRequestFailure();
                        return;
                    }
                    List<LostTable> lostTables = response.body().getLostTables();
                    if(lostTables!=null) {
                        for (LostTable lostTable : lostTables) {
                            itemReceiver.onItemArrived(new LfItem(lostTable));
                        }
                    }
                    itemReceiver.onItemArrived(null);

                }

                @Override
                public void onFailure(Call<LostTableList> call, Throwable t) {
                    Log.d(TAG, "onFailure: ");

                    itemReceiver.onRequestFailure();
                }
            };
            API.user_getLostItems(owner).enqueue(callback);
        }


        private static void requestByFilter(final Request<LfItem> request, final MultipleSourcesItemReceiver<LfItem> msItemReceiver) {
            ItemsQuery query = (ItemsQuery) request.getQuery();
            msItemReceiver.onWorkerStarted();
            Callback<LostTableList> callback = new Callback<LostTableList>() {
                @Override
                public void onResponse(Call<LostTableList> call, Response<LostTableList> response) {
                    if (!response.isSuccessful()) {
                        msItemReceiver.onRequestFailure();
                        return;
                    }
                    List<LostTable> lst = response.body().getLostTables();
                    if(lst!=null){
                        for (LostTable l : lst) {
                            msItemReceiver.onItemArrived(new LfItem(l));
                        }
                    }
                    msItemReceiver.onItemArrived(null);


                }

                @Override
                public void onFailure(Call<LostTableList> call, Throwable t) {
                    msItemReceiver.onRequestFailure();
                }
            };
            API.lost_queryItems(query.getName(), query.getDescription(), query.getLocation()).enqueue(callback);

        }

        public static void updateItem(final ItemReceiver<Boolean> itemReceiver, LfItem newItem) {
            API.lost_edit(newItem.toLostTable()).enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if(!response.isSuccessful()) {
                        itemReceiver.onRequestFailure();
                        return;
                    }
                    itemReceiver.onItemArrived(true);
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    itemReceiver.onRequestFailure();
                }
            });
        }

        public static void addItem(final ItemReceiver<Boolean> itemReceiver,LfItem lfItem) {
            Callback<Integer> callback = new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if(!response.isSuccessful()){
                        itemReceiver.onRequestFailure();
                        return;
                    }
                    itemReceiver.onItemArrived(true);
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    itemReceiver.onRequestFailure();
                }
            };
            API.lost_create(lfItem.toLostTable()).enqueue(callback);
        }
    }

    private class MultipleSourcesItemReceiver<T> implements ItemReceiver<T> {
        private int workingWorkersCount = 0;
        private ItemReceiver<T> parentItemReceiver;

        public MultipleSourcesItemReceiver(ItemReceiver<T> parentItemReceiver) {
            this.parentItemReceiver = parentItemReceiver;
        }

        public void onWorkerStarted() {
            workingWorkersCount++;
        }

        @Override
        public void onItemArrived(T item) {
            if (item == null) {
                workingWorkersCount--;
                if (workingWorkersCount == 0) {
                    parentItemReceiver.onItemArrived(null);
                }
                return;
            }
            onItemArrived(item);
        }

        @Override
        public void onRequestFailure() {
            parentItemReceiver.onRequestFailure();
        }
    }

}
