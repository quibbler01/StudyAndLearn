package cn.quibbler.coroutine.kotlin;

public class PermissionRequester {

    public void requestPermissionWithArray(String[] permission) {

    }

    public void requestPermissions(String... permission) {

    }

    public static String[] STRS = {"quibbler", "potter"};

    public void test() {
        String[] strs = {"quibbler", "potter"};


        requestPermissionWithArray(strs);

        requestPermissions(strs);
    }

}
