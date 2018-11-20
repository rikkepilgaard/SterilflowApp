package com.example.sterilflowapp.presenter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

class ViewPagerAdapter extends FragmentPagerAdapter {

    //https://www.youtube.com/watch?v=G7CnD7p8PkE

    private FragmentManager fm;
    private FragmentTransaction ft;

    private List<String> fragmentTitle = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
        this.ft = fm.beginTransaction();
    }


    public void addFragment(Fragment fragment, String title){
        //ft.add(fragment,""+title);
        fragmentList.add(fragment);
        fragmentTitle.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position){
        return fragmentTitle.get(position);
    }


}