package com.jlpay.kotlindemo.bean;

public class PermissionBean {
    private String permission;//权限，如：Manifest.permission.CAMERA
    private String permissionName;//权限名称，如：相机，存储
    private String permissionDes;//权限申请原因描述，如：APP需要获取您的位置信息，用于工单处理等需要位置信息的功能
    private boolean granted;//权限是否已经被授权，如：true，已授权；  false，未授权

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionDes() {
        return permissionDes;
    }

    public void setPermissionDes(String permissionDes) {
        this.permissionDes = permissionDes;
    }

    public boolean isGranted() {
        return granted;
    }

    public void setGranted(boolean granted) {
        this.granted = granted;
    }
}
