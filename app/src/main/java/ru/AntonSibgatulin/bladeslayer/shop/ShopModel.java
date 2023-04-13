package ru.AntonSibgatulin.bladeslayer.shop;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONObject;

import java.util.HashMap;

import ru.AntonSibgatulin.bladeslayer.R;
import ru.AntonSibgatulin.bladeslayer.ShopFragment;

public class ShopModel {
    public JSONObject json = null;
    public ShopFragment shopFragment = null;
//ShopLoadingActivityFragment

    public ShopModel(JSONObject jsonObject){
        this.json = jsonObject;

    }
    public void setShopFragment(ShopFragment shopFragment){
        this.shopFragment = shopFragment;
    }

}
