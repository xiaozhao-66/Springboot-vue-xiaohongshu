import { ZIMConversationType, } from './ZIMDefines';
import { ZIMManager } from './impl/ZIMManager';
export default class ZIM {
    engine;
    static _instatance = null;
    constructor(engine) {
        if (!engine)
            throw new Error('Can not be use new.');
        this.engine = engine;
    }
    /**
    * Gets the SDK's version number.
    *
    * When the SDK is running, the developer finds that it does not match the expected situation and submits the problem and related logs to the ZEGO technical staff for locating. The ZEGO technical staff may need the information of the engine version to assist in locating the problem.
    * Developers can also collect this information as the version information of the engine used by the app, so that the SDK corresponding to each version of the app on the line.
    * @return {string} - SDK version
    */
    static getVersion() {
        return ZIMManager.getInstance().getVersion();
    }
    static create(appConfig) {
        const appID = typeof appConfig == 'object' ? appConfig.appID : appConfig;
        const appSign = typeof appConfig == 'object' ? appConfig.appSign : '';
        const engine = ZIMManager.getInstance().createEngine(appID, appSign);
        if (engine) {
            engine.create();
            ZIM._instatance = new ZIM(engine);
            return ZIM._instatance;
        }
        return null;
    }
    static getInstance() {
        return ZIM._instatance;
    }
    destroy() {
        ZIMManager.getInstance().destroyEngine(this.engine);
        // @ts-ignore
        this.engine = null;
        ZIM._instatance = null;
    }
    static setLogConfig(config) {
        ZIMManager.getInstance().setLogConfig(config);
    }
    static setCacheConfig(config) {
        ZIMManager.getInstance().setCacheConfig(config);
    }
    on(type, listener) {
        this.engine.on(type, listener);
    }
    off(type) {
        this.engine.off(type);
    }
    uploadLog() {
        return this.engine.uploadLog();
    }
    login(userInfo, token) {
        return this.engine.login(userInfo, token);
    }
    logout() {
        return this.engine.logout();
    }
    renewToken(token) {
        return this.engine.renewToken(token);
    }
    updateUserName(userName) {
        return this.engine.updateUserName(userName);
    }
    updateUserAvatarUrl(userAvatarUrl) {
        return this.engine.updateUserAvatarUrl(userAvatarUrl);
    }
    updateUserExtendedData(extendedData) {
        return this.engine.updateUserExtendedData(extendedData);
    }
    queryUsersInfo(userIDs, config) {
        return this.engine.queryUsersInfo(userIDs, config);
    }
    // MARK: - Conversation
    queryConversationList(config) {
        return this.engine.queryConversationList(config);
    }
    deleteConversation(conversationID, conversationType, config) {
        return this.engine.deleteConversation(conversationID, conversationType, config);
    }
    setConversationNotificationStatus(status, conversationID, conversationType) {
        return this.engine.setConversationNotificationStatus(status, conversationID, conversationType);
    }
    clearConversationUnreadMessageCount(conversationID, conversationType) {
        return this.engine.clearConversationUnreadMessageCount(conversationID, conversationType);
    }
    sendMessage(message, toConversationID, conversationType, config, notification) {
        return this.engine.sendMessage(message, toConversationID, conversationType, config, notification);
    }
    /**
     * @deprecated Replaced with sendMessage.
     */
    sendPeerMessage(message, toUserID, config) {
        return this.engine.sendMessage(message, toUserID, ZIMConversationType.Peer, config);
    }
    /**
     * @deprecated Replaced with sendMessage.
     */
    sendGroupMessage(message, toGroupID, config) {
        return this.engine.sendMessage(message, toGroupID, ZIMConversationType.Group, config);
    }
    /**
     * @deprecated Replaced with sendMessage.
     */
    sendRoomMessage(message, toRoomID, config) {
        return this.engine.sendMessage(message, toRoomID, ZIMConversationType.Room, config);
    }
    sendMediaMessage(message, toConversationID, conversationType, config, notification) {
        return this.engine.sendMediaMessage(message, toConversationID, conversationType, config, notification);
    }
    deleteMessages(messageList, conversationID, conversationType, config) {
        return this.engine.deleteMessages(messageList, conversationID, conversationType, config);
    }
    deleteAllMessage(conversationID, conversationType, config) {
        return this.engine.deleteAllMessage(conversationID, conversationType, config);
    }
    queryHistoryMessage(conversationID, conversationType, config) {
        return this.engine.queryHistoryMessage(conversationID, conversationType, config);
    }
    insertMessageToLocalDB(message, conversationID, conversationType, senderUserID) {
        return this.engine.insertMessageToLocalDB(message, conversationID, conversationType, senderUserID);
    }
    downloadMediaFile(message, fileType, progress) {
        return this.engine.downloadMediaFile(message, fileType, progress);
    }
    // MARK: - Room
    createRoom(roomInfo, config) {
        return this.engine.createRoom(roomInfo, config);
    }
    enterRoom(roomInfo, config) {
        return this.engine.enterRoom(roomInfo, config);
    }
    joinRoom(roomID) {
        return this.engine.joinRoom(roomID);
    }
    leaveRoom(roomID) {
        return this.engine.leaveRoom(roomID);
    }
    queryRoomMemberList(roomID, config) {
        return this.engine.queryRoomMemberList(roomID, config);
    }
    queryRoomOnlineMemberCount(roomID) {
        return this.engine.queryRoomOnlineMemberCount(roomID);
    }
    queryRoomAllAttributes(roomID) {
        return this.engine.queryRoomAllAttributes(roomID);
    }
    setRoomAttributes(roomAttributes, roomID, config) {
        return this.engine.setRoomAttributes(roomAttributes, roomID, config);
    }
    deleteRoomAttributes(keys, roomID, config) {
        return this.engine.deleteRoomAttributes(keys, roomID, config);
    }
    beginRoomAttributesBatchOperation(roomID, config) {
        this.engine.beginRoomAttributesBatchOperation(roomID, config);
    }
    endRoomAttributesBatchOperation(roomID) {
        return this.engine.endRoomAttributesBatchOperation(roomID);
    }
    setRoomMembersAttributes(attributes, userIDs, roomID, config) {
        return this.engine.setRoomMembersAttributes(attributes, userIDs, roomID, config);
    }
    queryRoomMembersAttributes(userIDs, roomID) {
        return this.engine.queryRoomMembersAttributes(userIDs, roomID);
    }
    queryRoomMemberAttributesList(roomID, config) {
        return this.engine.queryRoomMemberAttributesList(roomID, config);
    }
    // MARK: - Group
    createGroup(groupInfo, userIDs, config) {
        return this.engine.createGroup(groupInfo, userIDs, config);
    }
    joinGroup(groupID) {
        return this.engine.joinGroup(groupID);
    }
    leaveGroup(groupID) {
        return this.engine.leaveGroup(groupID);
    }
    dismissGroup(groupID) {
        return this.engine.dismissGroup(groupID);
    }
    queryGroupList() {
        return this.engine.queryGroupList();
    }
    updateGroupNotice(groupNotice, groupID) {
        return this.engine.updateGroupNotice(groupNotice, groupID);
    }
    updateGroupName(groupName, groupID) {
        return this.engine.updateGroupName(groupName, groupID);
    }
    updateGroupAvatarUrl(groupAvatarUrl, groupID) {
        return this.engine.updateGroupAvatarUrl(groupAvatarUrl, groupID);
    }
    queryGroupInfo(groupID) {
        return this.engine.queryGroupInfo(groupID);
    }
    setGroupAttributes(groupAttributes, groupID) {
        return this.engine.setGroupAttributes(groupAttributes, groupID);
    }
    deleteGroupAttributes(keys, groupID) {
        return this.engine.deleteGroupAttributes(keys, groupID);
    }
    queryGroupAttributes(keys, groupID) {
        return this.engine.queryGroupAttributes(keys, groupID);
    }
    queryGroupAllAttributes(groupID) {
        return this.engine.queryGroupAllAttributes(groupID);
    }
    setGroupMemberNickname(nickname, forUserID, groupID) {
        return this.engine.setGroupMemberNickname(nickname, forUserID, groupID);
    }
    setGroupMemberRole(role, forUserID, groupID) {
        return this.engine.setGroupMemberRole(role, forUserID, groupID);
    }
    transferGroupOwner(toUserID, groupID) {
        return this.engine.transferGroupOwner(toUserID, groupID);
    }
    queryGroupMemberInfo(userID, groupID) {
        return this.engine.queryGroupMemberInfo(userID, groupID);
    }
    inviteUsersIntoGroup(userIDs, groupID) {
        return this.engine.inviteUsersIntoGroup(userIDs, groupID);
    }
    kickGroupMembers(userIDs, groupID) {
        return this.engine.kickGroupMembers(userIDs, groupID);
    }
    queryGroupMemberList(groupID, config) {
        return this.engine.queryGroupMemberList(groupID, config);
    }
    queryGroupMemberCount(groupID) {
        return this.engine.queryGroupMemberCount(groupID);
    }
    // MARK: - Call
    callInvite(invitees, config) {
        return this.engine.callInvite(invitees, config);
    }
    callCancel(invitees, callID, config) {
        return this.engine.callCancel(invitees, callID, config);
    }
    callAccept(callID, config) {
        return this.engine.callAccept(callID, config);
    }
    callReject(callID, config) {
        return this.engine.callReject(callID, config);
    }
}
