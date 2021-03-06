package xzh.com.wangyinews.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import java.util.List;

import xzh.com.wangyinews.R;
import xzh.com.wangyinews.utils.UIUtil;

public class BaseActivity extends FragmentActivity {
    private boolean flagBarTint = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        setContentView(View.inflate(this, layoutResID, null));
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initBarTint(view);
    }

    //解决activity嵌套fragment onActivityResult不执行的问题
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FragmentManager fm = getSupportFragmentManager();
        int index = requestCode >> 16;
        if (index != 0) {
            index--;
            if (fm.getFragments() == null || index < 0 || index >= fm.getFragments().size()) {
                return;
            }
            Fragment frag = fm.getFragments().get(index);
            if (frag != null) {
                handleResult(frag, requestCode, resultCode, data);
            }
            return;
        }
    }

    private void handleResult(Fragment frag, int requestCode, int resultCode, Intent data) {
        frag.onActivityResult(requestCode & 0xffff, resultCode, data);
        List<Fragment> frags = frag.getChildFragmentManager().getFragments();
        if (frags == null) return;
        for (Fragment f : frags) {
            if (f != null)
                handleResult(f, requestCode, resultCode, data);
        }
    }

    private void initBarTint(View view) {
        if (!flagBarTint) return;
//        UIUtil.initTintMgr(this, view).setStatusBarTintResource(R.drawable.bar_tint_shape);
    }

    protected void flagBarTint(boolean bool) {
        flagBarTint = bool;
    }

}
