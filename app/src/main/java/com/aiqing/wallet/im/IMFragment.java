package com.aiqing.wallet.im;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiqing.basiclibrary.utils.DensityUtil;
import com.aiqing.im.main.helper.SystemMessageUnreadManager;
import com.aiqing.im.main.reminder.ReminderItem;
import com.aiqing.im.main.reminder.ReminderManager;
import com.aiqing.wallet.R;
import com.aiqing.wallet.common.base.BaseFragment;
import com.aiqing.wallet.im.main.fragment.MainTab;
import com.aiqing.wallet.im.main.fragment.MainTabFragment;
import com.netease.nim.uikit.common.fragment.TabFragment;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.SystemMessageService;

import java.util.List;


public class IMFragment extends BaseFragment implements TabFragment.State, ReminderManager.UnreadNumChangedCallback {
    MainTabFragment sessionListFragment;
    MainTabFragment contactListFragment;
    FragmentManager fm;
    MainTabFragment currentFragment;
    FrameLayout flSession, flContact;

    public IMFragment() {
    }

    public static IMFragment newInstance() {
        IMFragment fragment = new IMFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onShow() {
        super.onShow();
        setWindowStatusBarColor(getResources().getColor(R.color.base_bg));
    }

    /**
     * 注册/注销系统消息未读数变化
     *
     * @param register
     */
    private void registerSystemMessageObservers(boolean register) {
        NIMClient.getService(SystemMessageObserver.class).observeUnreadCountChange(sysMsgUnreadCountChangedObserver,
                register);
    }

    private Observer<Integer> sysMsgUnreadCountChangedObserver = new Observer<Integer>() {
        @Override
        public void onEvent(Integer unreadCount) {
            SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unreadCount);
            ReminderManager.getInstance().updateContactUnreadNum(unreadCount);
        }
    };

    /**
     * 查询系统消息未读数
     */
    private void requestSystemMessageUnreadCount() {
        int unread = NIMClient.getService(SystemMessageService.class).querySystemMessageUnreadCountBlock();
        SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unread);
        ReminderManager.getInstance().updateContactUnreadNum(unread);
    }

    /**
     * 注册未读消息数量观察者
     */
    private void registerMsgUnreadInfoObserver(boolean register) {
        if (register) {
            ReminderManager.getInstance().registerUnreadNumChangedCallback(this);
        } else {
            ReminderManager.getInstance().unregisterUnreadNumChangedCallback(this);
        }
    }

    private void showSessionList(boolean show) {
        navBack.setVisibility(show ? View.GONE : View.VISIBLE);
        navTitle.setText(show ? R.string.s_free_session : R.string.s_free_contact);
        tvMenu.setVisibility(show ? View.VISIBLE : View.GONE);
        if (show) {
            currentFragment = sessionListFragment;
            flSession.setVisibility(View.VISIBLE);
            flContact.setVisibility(View.GONE);
        } else {
            currentFragment = contactListFragment;
            flSession.setVisibility(View.GONE);
            flContact.setVisibility(View.VISIBLE);
        }
    }

    ImageView navBack;
    TextView navTitle;
    TextView tvMenu;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fm = getChildFragmentManager();
        View view = inflater.inflate(R.layout.frg_im, container, false);
        tvMenu = view.findViewById(R.id.nav_menu);
        Drawable drawable = getResources().getDrawable(R.drawable.iv_im_contact_list);
//        drawable.setBounds(0, 0, DensityUtil.dip2px(getContext(), 12), DensityUtil.dip2px(getContext(), 6));
        drawable.setBounds(0, 0, DensityUtil.dip2px(getContext(), 18), DensityUtil.dip2px(getContext(), 21));
        tvMenu.setCompoundDrawables(null, null, drawable, null);
        tvMenu.setVisibility(View.VISIBLE);
        tvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSessionList(false);
            }
        });
        navBack = view.findViewById(R.id.nav_back);
        navBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSessionList(true);
            }
        });
        navTitle = view.findViewById(R.id.nav_title);
        navTitle.setText(getString(R.string.s_free_session));
        sessionListFragment = newFrg(MainTab.RECENT_CONTACTS);
        contactListFragment = newFrg(MainTab.CONTACT);
        if (!sessionListFragment.isAdded()) {
            fm.beginTransaction().add(R.id.frg_sessionlist, sessionListFragment).commit();
        } else {
            fm.beginTransaction().show(sessionListFragment).commit();
        }
        if (!contactListFragment.isAdded()) {
            fm.beginTransaction().add(R.id.frg_contactlist, contactListFragment).commit();
        } else {
            fm.beginTransaction().show(contactListFragment).commit();
        }
        flSession = view.findViewById(R.id.frg_sessionlist);
        flContact = view.findViewById(R.id.frg_contactlist);

        registerMsgUnreadInfoObserver(true);
        registerSystemMessageObservers(true);
        requestSystemMessageUnreadCount();
        showSessionList(true);
        return view;
    }

    private MainTabFragment newFrg(MainTab tab) {
        MainTabFragment fragment = null;
        try {
            List<Fragment> fs = fm.getFragments();
            if (fs != null) {
                for (Fragment f : fs) {
                    if (f.getClass() == tab.clazz) {
                        fragment = (MainTabFragment) f;
                        break;
                    }
                }
            }
            if (fragment == null) {
                fragment = tab.clazz.newInstance();
            }
            fragment.setState(this);
            fragment.attachTabData(tab);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        }
        return fragment;
    }

    @Override
    public boolean isCurrent(TabFragment fragment) {
        return currentFragment == fragment;
    }

    @Override
    public void onUnreadNumChanged(ReminderItem item) {
//        MainTab tab = MainTab.fromReminderId(item.getId());
//        if (tab != null) {
//            tabs.upd ateTab(tab.tabIndex, item);
//        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        registerMsgUnreadInfoObserver(false);
        registerSystemMessageObservers(false);
    }
}
