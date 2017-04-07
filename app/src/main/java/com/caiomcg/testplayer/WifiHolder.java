package com.caiomcg.testplayer;

import android.content.Context;
import android.net.wifi.WifiManager;

/**
 * Created by caiomcg on 07/04/17.
 */

public class WifiHolder {
    private WifiManager wifiManager;
    private Object lock;
    public  LockType lockType;
    private boolean locked;

    public enum LockType {
        Multicast, Default;

        public Object getLock(WifiManager manager) {
            if (this.equals(LockType.Multicast)) {
                return manager.createMulticastLock("Multi-Lock");
            }
            return  manager.createWifiLock("Wifi-Lock");
        }
    }

    public WifiHolder(Context appContext, LockType lock) {
        this.lockType    = lock;
        this.wifiManager = (WifiManager) appContext.getApplicationContext().getSystemService(appContext.getApplicationContext().WIFI_SERVICE);
        this.lock        = lock.getLock(this.wifiManager);
        this.locked      = false;
    }

    public void lock() {
        if (this.lockType.equals(LockType.Multicast)) {
            WifiManager.MulticastLock lock = (WifiManager.MulticastLock) this.lock;
            lock.acquire();
        } else {
            WifiManager.WifiLock lock = (WifiManager.WifiLock) this.lock;
            lock.acquire();
        }
        locked = true;
    }

    public void unlock() {
        if (this.lockType.equals(LockType.Multicast)) {
            WifiManager.MulticastLock lock = (WifiManager.MulticastLock) this.lock;
            lock.release();
        } else {
            WifiManager.WifiLock lock = (WifiManager.WifiLock) this.lock;
            lock.release();
        }
        locked = false;
    }

    public boolean isLocked() {
        return locked;
    }
}
