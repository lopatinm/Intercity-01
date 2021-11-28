package com.lopatinm.intercity.ui.order;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.google.android.material.tabs.TabLayout;
import com.lopatinm.intercity.MainActivity;
import com.lopatinm.intercity.R;

public class OrderFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_order, container, false);

        GetorderFragment getorderFragment = new GetorderFragment();
        HitchhikingFragment hitchhikingFragment = new HitchhikingFragment();
        OrderuserFragment orderuserFragment = new OrderuserFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.order_container, getorderFragment);
        transaction.commit();

        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.order_tabs);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tabLayout.getSelectedTabPosition()) {
                    case 0:
                        FragmentTransaction transaction0 = getFragmentManager().beginTransaction();
                        transaction0.replace(R.id.order_container, getorderFragment);
                        transaction0.commit();
                        break;
                    case 1:
                        FragmentTransaction transaction1 = getFragmentManager().beginTransaction();
                        transaction1.replace(R.id.order_container, hitchhikingFragment);
                        transaction1.commit();
                        break;
                    case 2:
                        FragmentTransaction transaction2 = getFragmentManager().beginTransaction();
                        transaction2.replace(R.id.order_container, orderuserFragment);
                        transaction2.commit();
                        break;

                    default:

                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return root;
    }
}