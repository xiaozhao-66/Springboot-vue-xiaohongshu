import { ZegoAnyCallback } from '../ZIMEventHandler';
import { ZIMCacheConfig, ZIMLogConfig } from "../ZIMDefines";
import { ZIMEngine } from './ZIMEngine';
export declare class ZIMManager {
    private static instance;
    private handle;
    static engineMap: Map<string, ZIMEngine>;
    static listeners: Map<string, Map<ZegoAnyCallback, ZegoAnyCallback>>;
    static getInstance(): ZIMManager;
    getVersion(): Promise<string>;
    createEngine(appID: number, appSign: string): ZIMEngine | null;
    destroyEngine(engine: ZIMEngine): Promise<void>;
    setLogConfig(config: ZIMLogConfig): Promise<void>;
    setCacheConfig(config: ZIMCacheConfig): Promise<void>;
}
