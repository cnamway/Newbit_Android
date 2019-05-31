package config;

import android.content.Context;

import com.spark.newbitrade.data.DataRepository;
import com.spark.newbitrade.data.LocalDataSource;
import com.spark.newbitrade.data.RemoteDataSource;

/**
 * Created by Administrator on 2017/9/25.
 */

public class Injection {
    public static DataRepository provideTasksRepository(Context context) {
        return DataRepository.getInstance(RemoteDataSource.getInstance(), LocalDataSource.getInstance(context));
    }
}
