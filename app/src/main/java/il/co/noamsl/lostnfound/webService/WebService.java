package il.co.noamsl.lostnfound.webService;

import android.util.Log;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import java.util.Arrays;
import java.util.List;

import il.co.noamsl.lostnfound.repository.User.User;
import il.co.noamsl.lostnfound.repository.item.LfItem;
import il.co.noamsl.lostnfound.webService.dataTransfer.ItemReceiver;
import il.co.noamsl.lostnfound.webService.dataTransfer.ItemsQuery;
import il.co.noamsl.lostnfound.webService.dataTransfer.Request;
import il.co.noamsl.lostnfound.webService.dataTransfer.RequestAgent;
import il.co.noamsl.lostnfound.webService.serverInternal.FoundTable;
import il.co.noamsl.lostnfound.webService.serverInternal.FoundTableList;
import il.co.noamsl.lostnfound.webService.serverInternal.LostTable;
import il.co.noamsl.lostnfound.webService.serverInternal.LostTableList;
import il.co.noamsl.lostnfound.webService.serverInternal.ServerAPI;
import il.co.noamsl.lostnfound.webService.serverInternal.Users;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;


public class WebService {
    private static final String TAG = "WebService";


    // '/' at the end is required
    private static final String BASE_URL_PHONE = "http://192.168.1.102:8080/lf_server/webresources/";
    private static final String BASE_URL_EMULATOR = "http://10.0.2.2:8080/lf_server/webresources/";
    // order at which converters are added matters!
    // Scalar converter should be added first.
    public static final ServerAPI API = new Retrofit.Builder()
            .baseUrl(BASE_URL_EMULATOR)
            .client(new OkHttpClient())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(
                    SimpleXmlConverterFactory.createNonStrict(
                            new Persister(new AnnotationStrategy())
                    )
            )
            .build().create(ServerAPI.class);
    private static final String BASE_URL_DEFAULT = BASE_URL_EMULATOR;
    private static final String[] BASE_URLS = {BASE_URL_PHONE, BASE_URL_EMULATOR};



    public void requestItems(final Request<LfItem> request, RequestAgent requestAgent) {


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
        ItemsQuery query = (ItemsQuery) request.getQuery();
        Boolean isAFound = query.isAFound();


        MultipleSourcesItemReceiver<LfItem> msItemReceiver = new MultipleSourcesItemReceiver<>(request.getItemReceiver());

        if (isAFound == null || isAFound) {
            Founds.requestItemsOfUser(request, query.getOwner(), msItemReceiver);
        }
        if (isAFound == null || !isAFound) {
            Losts.requestItemsOfUser(request, query.getOwner(), msItemReceiver);
        }

    }


    private void requestItemsByFilter(final Request<LfItem> request) {

        ItemsQuery query = (ItemsQuery) request.getQuery();
        MultipleSourcesItemReceiver<LfItem> msItemReceiver = new MultipleSourcesItemReceiver<>(request.getItemReceiver());
        Boolean isAFound = query.isAFound();


        if (isAFound == null || isAFound) {
            Founds.requestByFilter(request, msItemReceiver);
        }
        if (isAFound == null || !isAFound) {
            Losts.requestByFilter(request, msItemReceiver);
        }

    }

    public void addItem(final ItemReceiver<Integer> itemReceiver, LfItem lfItem) {
        if (lfItem.isAFound()) {
            Founds.addItem(itemReceiver, lfItem);
        } else if (lfItem.isALost()) {
            Losts.addItem(itemReceiver, lfItem);
        } else {
            throw new RuntimeException("LfItem must be a lost or a found");
        }
    }

    public void updateItem(ItemReceiver<Boolean> itemReceiver, LfItem newItem) {
        itemReceiver.onItemArrived(true);
        if (newItem.isALost()) {
            Losts.updateItem(itemReceiver, newItem);
        }
        if (newItem.isAFound()) {
            Founds.updateItem(itemReceiver, newItem);

        }
    }

    public void getUserById(final ItemReceiver<User> itemReceiver, int userId) {
        Callback<Users> callback = new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (!response.isSuccessful() || response.body() == null) {
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
        API.user_getSettings(userId, null).enqueue(callback);
    }

    public void getUserByCredential(final ItemReceiver<User> itemReceiver, String credential) {
        Callback<Users> callback = new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {


                if (!response.isSuccessful()) {
                    itemReceiver.onRequestFailure();
                    return;
                }
                if (response.body() == null) {
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
        API.user_getSettings(null, credential).enqueue(callback);
    }

    public void updateUser(final ItemReceiver<User> itemReceiver, final User user) {
        Callback<Integer> callback = new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (!response.isSuccessful()) {
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

    //assumes legal registration email
    public void registerUser(final ItemReceiver<User> userItemReceiver, final String mEmail) {

        API.user_create(new Users(null, mEmail, null, null, null)).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {

                if (!response.isSuccessful() || response.body() == null) {
                    userItemReceiver.onRequestFailure();
                }
                getUserByCredential(userItemReceiver, mEmail);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

                userItemReceiver.onRequestFailure();
            }
        });
    }

    private static class Founds {
        private static final String TAG = "WebService.Founds";

        private static void requestItemsOfUser(Request<LfItem> request, Integer owner, MultipleSourcesItemReceiver<LfItem> msItemReceiver) {


            final ItemReceiver<LfItem> itemReceiver = msItemReceiver;
            msItemReceiver.onWorkerStarted();
            Callback<FoundTableList> callback = new Callback<FoundTableList>() {
                @Override
                public void onResponse(Call<FoundTableList> call, Response<FoundTableList> response) {
                    if (!response.isSuccessful()) {
                        itemReceiver.onRequestFailure();
                        return;
                    }
                    if (response.body() != null) {
                        List<FoundTable> foundTables = response.body().getFoundTables();
                        if (foundTables != null) {
                            for (FoundTable foundTable : foundTables) {
                                itemReceiver.onItemArrived(new LfItem(foundTable));

                            }
                        }
                    }
                    itemReceiver.onItemArrived(null);

                }

                @Override
                public void onFailure(Call<FoundTableList> call, Throwable t) {


                    itemReceiver.onRequestFailure();
                }
            };
            API.user_getFoundItems(owner).enqueue(callback);
        }

        private static void requestByFilter(final Request<LfItem> request, final MultipleSourcesItemReceiver<LfItem> msItemReceiver) {
            final ItemsQuery query = (ItemsQuery) request.getQuery();

            msItemReceiver.onWorkerStarted();
            Callback<FoundTableList> callback = new Callback<FoundTableList>() {
                @Override
                public void onResponse(Call<FoundTableList> call, Response<FoundTableList> response) {
                    if (!response.isSuccessful()) {
                        Log.d(TAG, "onResponse not successful: query = " + Arrays.toString(new String[]{query.getName(), query.getDescription(), query.getLocation()}));
                        Log.d(TAG, "onResponse: response.raw() = " + response.raw());

                        msItemReceiver.onRequestFailure();
                        return;
                    }
                    if(response.body() != null){
                        List<FoundTable> lst = response.body().getFoundTables();
                        if (lst != null) {
                            for (FoundTable l : lst) {
                                Log.d(TAG, "onResponse: l = " + l);

                                msItemReceiver.onItemArrived(new LfItem(l));
                            }
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
                    if (!response.isSuccessful()) {
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

        public static void addItem(final ItemReceiver<Integer> itemReceiver, LfItem lfItem) {

            Callback<Integer> callback = new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {


                    if (!response.isSuccessful()) {
                        itemReceiver.onRequestFailure();
                        return;
                    }
                    itemReceiver.onItemArrived(response.body());
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {


                    itemReceiver.onRequestFailure();
                }
            };


            API.found_create(lfItem.toFoundTable()).enqueue(callback); //// FIXME: 18/11/2017

        }
    }

    private static class Losts {
        private static final String TAG = "WebService.Losts";

        private static void requestItemsOfUser(Request<LfItem> request, Integer owner, MultipleSourcesItemReceiver<LfItem> msItemReceiver) {


            final ItemReceiver<LfItem> itemReceiver = msItemReceiver;
            msItemReceiver.onWorkerStarted();
            Callback<LostTableList> callback = new Callback<LostTableList>() {
                @Override
                public void onResponse(Call<LostTableList> call, Response<LostTableList> response) {
                    if (!response.isSuccessful()) {
                        itemReceiver.onRequestFailure();
                        return;
                    }
                    if (response.body() != null) {
                        List<LostTable> lostTables = response.body().getLostTables();
                        if (lostTables != null) {
                            for (LostTable lostTable : lostTables) {
                                itemReceiver.onItemArrived(new LfItem(lostTable));

                            }
                        }
                    }
                    itemReceiver.onItemArrived(null);

                }

                @Override
                public void onFailure(Call<LostTableList> call, Throwable t) {


                    itemReceiver.onRequestFailure();
                }
            };
            API.user_getLostItems(owner).enqueue(callback);
        }

        private static void requestByFilter(final Request<LfItem> request, final MultipleSourcesItemReceiver<LfItem> msItemReceiver) {
            final ItemsQuery query = (ItemsQuery) request.getQuery();

            msItemReceiver.onWorkerStarted();
            Callback<LostTableList> callback = new Callback<LostTableList>() {
                @Override
                public void onResponse(Call<LostTableList> call, Response<LostTableList> response) {
                    if (!response.isSuccessful()) {
                        Log.d(TAG, "onResponse not successful: query = " + Arrays.toString(new String[]{query.getName(), query.getDescription(), query.getLocation()}));
                        Log.d(TAG, "onResponse: response.raw() = " + response.raw());

                        msItemReceiver.onRequestFailure();
                        return;
                    }
                    if(response.body() != null){
                        List<LostTable> lst = response.body().getLostTables();
                        if (lst != null) {
                            for (LostTable l : lst) {
                                Log.d(TAG, "onResponse: l = " + l);

                                msItemReceiver.onItemArrived(new LfItem(l));
                            }
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
                    if (!response.isSuccessful()) {
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

        public static void addItem(final ItemReceiver<Integer> itemReceiver, LfItem lfItem) {

            Callback<Integer> callback = new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {


                    if (!response.isSuccessful()) {
                        itemReceiver.onRequestFailure();
                        return;
                    }
                    itemReceiver.onItemArrived(response.body());
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {


                    itemReceiver.onRequestFailure();
                }
            };


            API.lost_create(lfItem.toLostTable()).enqueue(callback); //// FIXME: 18/11/2017

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
            parentItemReceiver.onItemArrived(item);
        }

        @Override
        public void onRequestFailure() {
            parentItemReceiver.onRequestFailure();
        }
    }

}
