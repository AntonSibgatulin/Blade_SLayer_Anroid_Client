package ru.AntonSibgatulin.bladeslayer.shop

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import org.json.JSONObject
import ru.AntonSibgatulin.bladeslayer.MainActivity
import ru.AntonSibgatulin.bladeslayer.Players.PlayerController
import ru.AntonSibgatulin.bladeslayer.R
import ru.AntonSibgatulin.bladeslayer.ShopFragment

class ShopModelKt {


    var isLoading: Boolean = true;
    var player: PlayerController? = null
    var json: JSONObject? = null
    var shopFragment: ShopFragment? = null

    constructor(json: JSONObject, playerController: PlayerController) {
        this.json = json
        this.player = playerController;
    }

    fun init(fragment: Fragment) {
        //var magicKey = json!!.getInt("magicKey")
        // var money = json!!.getInt("money");
        isLoading = true;


        if (this.json!!.has("players")) {
            for (i in 0..(this.json!!.getJSONArray("players").length() - 1)) {
                var jsonObject = json!!.getJSONArray("players").getJSONObject(i)
                var price = jsonObject.getInt("price")
                var id = jsonObject.getString("id")
                var price_magicCard = jsonObject.getInt("magicKey")
                if (player != null && isHas(id)) continue
                var score = jsonObject.getInt("score")
                var count_of_cards = jsonObject.getInt("count_of_cards")
                var magicKey = jsonObject.getInt("magicKey")
                var bitmap =
                    MainActivity.playerControllerStatic.playerPreviewController.hash.get(id)!!.image_sources

                if (bitmap == null) {
                }
                var name =
                    MainActivity.playerControllerStatic.playerPreviewController.hash.get(id)!!.name
                var jsonPlayer =
                    MainActivity.playerControllerStatic.playerPreviewController.hash.get(id)!!.json
                var type = jsonObject.getInt("type");
                if (jsonPlayer.getJSONArray("levels")
                        .length() > 0 && jsonPlayer.getJSONArray("levels")
                        .getJSONObject(type) != null
                ) {
                    var playerConfig = jsonPlayer.getJSONArray("levels").getJSONObject(type);


                    var fragmentManager: FragmentManager = fragment.requireFragmentManager()


                    var fragmentTransaction = fragmentManager
                        .beginTransaction()

                    // добавляем фрагмент

                    // добавляем фрагмент
                    var myFragment = PlayerShopFragment(
                        "",
                        id,
                        name,
                        price,
                        price_magicCard,
                        score,
                        magicKey,
                        count_of_cards,
                        playerConfig,
                        bitmap,
                        i
                    );
                    fragmentTransaction.add(R.id.shop_elements, myFragment)
                    fragmentTransaction.commit()
                }

            }
            isLoading = false;

        }


        if (this.json!!.has("magicKey")) {
            var money = this.json!!.getJSONArray("magicKey");
            for (i in 0..(money.length() - 1)) {
                var price = money.getJSONObject(i).getInt("price")

                var count = money.getJSONObject(i).getInt("count")
                var myFragment = MagicKeyFragment("", price, count);

                var fragmentManager: FragmentManager = fragment.requireFragmentManager()


                var fragmentTransaction = fragmentManager
                    .beginTransaction()

                // добавляем фрагмент

                // добавляем фрагмент
                fragmentTransaction.add(R.id.shop_elements, myFragment)
                fragmentTransaction.commit()
            }
        }


        if (this.json!!.has("money")) {
            var money = this.json!!.getJSONArray("money");
            for (i in 0..(money.length() - 1)) {
                var price = money.getJSONObject(i).getInt("price")

                var count = money.getJSONObject(i).getInt("count")

                var magicKey = money.getJSONObject(i).getInt("magicKey")

                var myFragment = MoneyFragment("", price, count, magicKey);

                var fragmentManager: FragmentManager = fragment.requireFragmentManager()


                var fragmentTransaction = fragmentManager
                    .beginTransaction()

                // добавляем фрагмент

                // добавляем фрагмент
                fragmentTransaction.add(R.id.shop_elements, myFragment)
                fragmentTransaction.commit()
            }
        }


    }

    fun isHas(id: String): Boolean {
        var players = player!!.json.getJSONArray("players");
        for (i in 0..(players.length() - 1)) {
            var plr = players.getJSONObject(i);
            if (plr.getString("id").equals(id)) return true;
        }
        return false;
    }
}