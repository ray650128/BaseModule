package tw.com.lig.module_base.widget;

import java.util.List;

/**
 * 动态权限獲取
 */

public interface PermissionListener {
    void granted();

    void denied(List<String> deniedList);
}
