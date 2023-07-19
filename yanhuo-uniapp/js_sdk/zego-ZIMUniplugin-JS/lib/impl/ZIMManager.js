import { ZIMEngine } from './ZIMEngine';
export class ZIMManager {
    static instance;
    handle = Symbol();
    static engineMap = new Map();
    static listeners = new Map();
    static getInstance() {
        return this.instance || (this.instance = new ZIMManager());
    }
    getVersion() {
        return ZIMEngine._callMethod("getVersion");
    }
    createEngine(appID, appSign) {
        // 设计上支持多实例：每次创建都会返回新的实例；但目前先做单实例
        /*
         const handle = Symbol();
         const engine = new ZIMEngine(handle, appID);
         this.enginMap.set(handle, engine);
         return engine;
         */
        const handle = this.handle;
        if (!ZIMManager.engineMap.get(handle.toString())) {
            const engine = new ZIMEngine(handle, appID, appSign);
            ZIMManager.engineMap.set(handle.toString(), engine);
            return engine;
        }
        return null;
    }
    destroyEngine(engine) {
        if (!engine) {
            return Promise.reject();
        }
        ZIMManager.engineMap.delete(engine.handle.toString());
        engine.uploadingMap.clear();
        engine.downloadingMap.clear();
        ZIMManager.listeners.clear();
        return engine.destroy();
    }
    setLogConfig(config) {
        return ZIMEngine._callMethod("setLogConfig", config);
    }
    setCacheConfig(config) {
        return ZIMEngine._callMethod("setCacheConfig", config);
    }
}
