package il.co.noamsl.lostnfound.webService;

import android.util.Log;

import il.co.noamsl.lostnfound.webService.eitan.FoundTable;
import il.co.noamsl.lostnfound.webService.eitan.FoundTableList;
import il.co.noamsl.lostnfound.webService.eitan.ServerAPI;
import il.co.noamsl.lostnfound.webService.eitan.Users;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//// FIXME: 19/11/2017 delete this
public class WSTest { //broken
    private static final String TAG = "WSTest";
    private final Users FAKE_USER = new Users("N", "a@gds.com", "050-1234567", "Hereeee", null);
    private final FoundTable FAKE_FOUND = new FoundTable("Wallet", "d", "l", null, "pic", null, true);
    private final Monitor monitor = new Monitor();
    private final ServerAPI TEST_API;
    private final int MAX_WAIT_MILLIES = 1000;
    private final Boolean[] gotResponse = {false};

    public WSTest(ServerAPI test_api) {
        TEST_API = test_api;
    }

    public void test() {
        Log.d(TAG, "test: testing");

        expectResponse();
        createUser();
        waitForPrevTask();
        Log.d(TAG, "test: 1");


        expectResponse();
        addFound();
        waitForPrevTask();
        Log.d(TAG, "test: 2");


        expectResponse();
        changeRelevant();
        waitForPrevTask();
        Log.d(TAG, "test: 3");


        expectResponse();
        getFounds();
        Log.d(TAG, "test: 4");


    }

    private void expectResponse() {
        gotResponse[0] = false;
    }

    private void waitForPrevTask() {
        /*try {
            synchronized (gotResponse) {
                if (!gotResponse[0]) {
                    synchronized (monitor) {
                        monitor.wait();
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        sleep();
    }

    private void getFounds() {
        TEST_API.found_queryItems("", "", null).enqueue(new Callback<FoundTableList>() {
            @Override
            public void onResponse(Call<FoundTableList> call, Response<FoundTableList> response) {
                if (!response.isSuccessful()) {
                    throw new RuntimeException("Server problem(onResponse): " + response.raw());
                } else {
                    Log.d(TAG, "got list = " + response.body().getFoundTables());
                }
            }

            @Override
            public void onFailure(Call<FoundTableList> call, Throwable t) {
                throw new RuntimeException("Server problem(onFailure) ");
            }
        });
    }

    private void changeRelevant() {
/*
        FoundTable changed = new FoundTable(FAKE_FOUND.getName(), FAKE_FOUND.getDescription(),
                FAKE_FOUND.getLocation(), FAKE_FOUND.getOwner(), FAKE_FOUND.getPicture(),
                FAKE_FOUND.getRecordid(), false);
*/
        Log.d(TAG, "changeRelevant: FAKE_FOUND before = " + FAKE_FOUND);
        FAKE_FOUND.setRelevant(false);
        TEST_API.found_edit(FAKE_FOUND).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                boomIfFailed(response);
                Log.d(TAG, "onResponse: changed = " + FAKE_FOUND);
                nextTask();

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                boom();
            }
        });
    }

    private void addFound() {
        Log.d(TAG, "addFound: FAKE_FOUND = " + FAKE_FOUND);

        TEST_API.found_create(FAKE_FOUND).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                boomIfFailed(response);
                FAKE_FOUND.setRecordid(response.body());
                nextTask();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                boom();
            }
        });
    }

    private void boomIfFailed(Response<Integer> response) {
        if (!response.isSuccessful() || response.body() == null || response.body() < 0) {
            throw new RuntimeException("Body = " + response.body());
        }
    }

    private void sleep() {
        try {
            Thread.sleep(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void createUser() {
        TEST_API.user_create(FAKE_USER).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                boomIfFailed(response);
                FAKE_USER.setUserid(response.body());
                FAKE_FOUND.setOwner(FAKE_USER.getUserid());
                nextTask();
            }


            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                boom();
            }
        });
    }

    private void nextTask() {
        synchronized (monitor) {
            gotResponse[0] = true;
            monitor.notify();
        }
    }

    private void boom() {
        throw new RuntimeException();
    }

    private class Monitor {
    }
}
