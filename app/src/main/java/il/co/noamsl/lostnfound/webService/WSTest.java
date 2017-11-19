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
public class WSTest {
    private static final String TAG = "WSTest";
    private final Users FAKE_USER = new Users("N", "a@gds.com", "050-1234567", "Hereeee", 777);
    private final FoundTable FAKE_FOUND = new FoundTable("Wallet", "d", "l", FAKE_USER.getUserid(), "pic", null, true);

    private final Monitor monitor = new Monitor();
    private final ServerAPI TEST_API;
    private final int MAX_WAIT_MILLIES = 1000;

    public WSTest(ServerAPI test_api) {
        TEST_API = test_api;
    }

    public void test() {
        Log.d(TAG, "test: testing");

        createUser();
        waitForPrevTask();
        addFound();
        waitForPrevTask();
        changeRelevant();
        waitForPrevTask();
        getFounds();

    }

    private void waitForPrevTask() {
        try {
            synchronized (monitor) {
                monitor.wait(MAX_WAIT_MILLIES);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getFounds() {
        TEST_API.found_queryItems("", "", null).enqueue(new Callback<FoundTableList>() {
            @Override
            public void onResponse(Call<FoundTableList> call, Response<FoundTableList> response) {
                if (!response.isSuccessful()) {
                    throw new RuntimeException("Server problem(onResponse): " + response.raw());
                } else {
                    Log.d(TAG, "allGood");
                }
            }

            @Override
            public void onFailure(Call<FoundTableList> call, Throwable t) {
                throw new RuntimeException("Server problem(onFailure) ");
            }
        });
    }

    private void changeRelevant() {
        FoundTable changed = new FoundTable(FAKE_FOUND.getName(), FAKE_FOUND.getDescription(),
                FAKE_FOUND.getLocation(), FAKE_FOUND.getOwner(), FAKE_FOUND.getPicture(),
                FAKE_FOUND.getRecordid(), false);
        TEST_API.found_edit(changed).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                boomIfFailed(response);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                boom();
            }
        });
    }

    private void addFound() {
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
        if (!response.isSuccessful()) {
            boom();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void createUser() {
        TEST_API.user_create(FAKE_USER).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                boomIfFailed(response);
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
            monitor.notify();
        }
        ;
    }

    private void boom() {
        throw new RuntimeException();
    }

    private class Monitor {
    }
}
