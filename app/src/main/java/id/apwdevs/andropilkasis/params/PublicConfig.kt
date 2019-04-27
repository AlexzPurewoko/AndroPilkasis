/**
 * Copyright @2019 by Alexzander Purwoko Widiantoro <purwoko908@gmail.com>
 * @author APWDevs
 *
 * Licensed under GNU GPLv3
 *
 * This module is provided by "AS IS" and if you want to take
 * a copy or modifying this module, you must include this @author
 * Thanks! Happy Coding!
 */

package id.apwdevs.andropilkasis.params

object PublicConfig {
    const val ALL_CATEGORIES = "All"
    const val DEFAULT_USER_AVATAR = "default"
    const val DEFAULT_CANDIDATE_AVATAR = DEFAULT_USER_AVATAR

    const val SHARED_PREF_NAME = "pilkasis_data"

    object ServerConfig {
        const val SERVER_TIMEOUT = 10000
        const val SERVER_URL = "http://192.168.1.1"
        const val BASE_DIR = "$SERVER_URL/AndroPilkasis"
        const val GET_USER_DATASHEET = "$BASE_DIR/get_user_datasheet.php"
        const val GET_LIST_CANDIDATES = "$BASE_DIR/get_all_kandidat_data.php"
        const val SELECT_CANDIDATE = "$BASE_DIR/select_kandidat.php"
        const val AUTH_LOGIN = "$BASE_DIR/login.php"
        const val GET_LIST_PARAMETERS = "$BASE_DIR/get_params.php"
        const val REALCOUNT_ALL = "$BASE_DIR/all_realcount.php"
        const val REALCOUNT_BY_CATEGORY = "$BASE_DIR/category_realcount.php"
        const val LIST_GROUPS = "$BASE_DIR/all_kelompok.php"
        const val ALLOW_ACCESS_REALCOUNT = "$BASE_DIR/allow_access.php"
        const val CANDIDATE_IMG_PATH = "$BASE_DIR/kandidat_img"
        const val USER_IMG_PATH = "$BASE_DIR/pemilih_img"
    }

    object ServerParams {
        const val KEY_ALLOW_ACCESS_REALCOUNT = "allow_user_access_realcount"
    }

    object SharedKey {
        const val KEY_ALL_CANDIDATE_LIST = "LIST_CANDIDATE"
        val KEY_USER_DATASHEET = "USER_DATASHEET"
        const val KEY_USERNAME = "username"
        const val KEY_PASSWORD = "password"
    }


}