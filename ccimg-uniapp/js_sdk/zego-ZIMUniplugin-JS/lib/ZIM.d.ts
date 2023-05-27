import { ZIMLogConfig, ZIMCacheConfig, ZIMUserInfo, ZIMMessage, ZIMMediaMessage, ZIMRoomInfo, ZIMGroupInfo, ZIMMediaFileType, ZIMConversationType, ZIMConversationNotificationStatus, ZIMTokenRenewedResult, ZIMUsersInfoQueriedResult, ZIMUserNameUpdatedResult, ZIMUserExtendedDataUpdatedResult, ZIMRoomAdvancedConfig, ZIMRoomAttributesSetConfig, ZIMRoomAttributesDeleteConfig, ZIMRoomAttributesBatchOperationConfig, ZIMRoomMemberQueryConfig, ZIMRoomCreatedResult, ZIMRoomEnteredResult, ZIMRoomJoinedResult, ZIMRoomLeftResult, ZIMRoomAttributesBatchOperatedResult, ZIMRoomAttributesOperatedResult, ZIMRoomAttributesQueriedResult, ZIMRoomMemberQueriedResult, ZIMRoomOnlineMemberCountQueriedResult, ZIMRoomMemberAttributesSetConfig, ZIMRoomMembersAttributesOperatedResult, ZIMRoomMembersAttributesQueriedResult, ZIMRoomMemberAttributesQueryConfig, ZIMRoomMemberAttributesListQueriedResult, ZIMGroupAdvancedConfig, ZIMGroupMemberQueryConfig, ZIMGroupCreatedResult, ZIMGroupJoinedResult, ZIMGroupLeftResult, ZIMGroupDismissedResult, ZIMGroupInfoQueriedResult, ZIMGroupListQueriedResult, ZIMGroupNameUpdatedResult, ZIMGroupNoticeUpdatedResult, ZIMGroupMemberKickedResult, ZIMGroupMemberListQueriedResult, ZIMGroupMemberNicknameUpdatedResult, ZIMGroupMemberRoleUpdatedResult, ZIMGroupMemberInfoQueriedResult, ZIMGroupAttributesOperatedResult, ZIMGroupAttributesQueriedResult, ZIMGroupUsersInvitedResult, ZIMGroupOwnerTransferredResult, ZIMGroupMemberCountQueriedResult, ZIMConversationQueryConfig, ZIMConversationDeleteConfig, ZIMConversationListQueriedResult, ZIMConversationDeletedResult, ZIMConversationNotificationStatusSetResult, ZIMConversationUnreadMessageCountClearedResult, ZIMMessageBase, ZIMMediaMessageBase, ZIMMessageSendConfig, ZIMMessageDeleteConfig, ZIMMessageQueryConfig, ZIMMessageSentResult, ZIMMediaMessageSentResult, ZIMMessageQueriedResult, ZIMMessageDeletedResult, ZIMMediaDownloadingProgress, ZIMMessageSendNotification, ZIMMediaMessageSendNotification, ZIMMediaDownloadedResult, ZIMCallInviteConfig, ZIMCallCancelConfig, ZIMCallAcceptConfig, ZIMCallRejectConfig, ZIMCallCancelSentResult, ZIMCallInvitationSentResult, ZIMCallAcceptanceSentResult, ZIMCallRejectionSentResult, ZIMAppConfig, ZIMUsersInfoQueryConfig, ZIMUserAvatarUrlUpdatedResult, ZIMGroupAvatarUrlUpdatedResult, ZIMMessageInsertedResult } from './ZIMDefines';
import { ZIMEventHandler } from './ZIMEventHandler';
export default class ZIM {
    private engine;
    private static _instatance;
    private constructor();
    /**
    * Gets the SDK's version number.
    *
    * When the SDK is running, the developer finds that it does not match the expected situation and submits the problem and related logs to the ZEGO technical staff for locating. The ZEGO technical staff may need the information of the engine version to assist in locating the problem.
    * Developers can also collect this information as the version information of the engine used by the app, so that the SDK corresponding to each version of the app on the line.
    * @return {string} - SDK version
    */
    static getVersion(): Promise<string>;
    /**
     * @Deprecated
     * Deprecated since 2.3.0, please use another create API
     */
    static create(appID: number): ZIM | null;
    static create(appConfig: ZIMAppConfig): ZIM | null;
    static getInstance(): ZIM | null;
    destroy(): void;
    static setLogConfig(config: ZIMLogConfig): void;
    static setCacheConfig(config: ZIMCacheConfig): void;
    on<K extends keyof ZIMEventHandler>(type: K, listener: ZIMEventHandler[K]): void;
    off<K extends keyof ZIMEventHandler>(type: K): void;
    uploadLog(): Promise<void>;
    login(userInfo: ZIMUserInfo, token?: string): Promise<void>;
    logout(): Promise<void>;
    renewToken(token: string): Promise<ZIMTokenRenewedResult>;
    updateUserName(userName: string): Promise<ZIMUserNameUpdatedResult>;
    updateUserAvatarUrl(userAvatarUrl: string): Promise<ZIMUserAvatarUrlUpdatedResult>;
    updateUserExtendedData(extendedData: string): Promise<ZIMUserExtendedDataUpdatedResult>;
    queryUsersInfo(userIDs: string[], config: ZIMUsersInfoQueryConfig): Promise<ZIMUsersInfoQueriedResult>;
    queryConversationList(config: ZIMConversationQueryConfig): Promise<ZIMConversationListQueriedResult>;
    deleteConversation(conversationID: string, conversationType: ZIMConversationType, config: ZIMConversationDeleteConfig): Promise<ZIMConversationDeletedResult>;
    setConversationNotificationStatus(status: ZIMConversationNotificationStatus, conversationID: string, conversationType: ZIMConversationType): Promise<ZIMConversationNotificationStatusSetResult>;
    clearConversationUnreadMessageCount(conversationID: string, conversationType: ZIMConversationType): Promise<ZIMConversationUnreadMessageCountClearedResult>;
    sendMessage(message: ZIMMessageBase, toConversationID: string, conversationType: ZIMConversationType, config: ZIMMessageSendConfig, notification?: ZIMMessageSendNotification): Promise<ZIMMessageSentResult>;
    /**
     * @deprecated Replaced with sendMessage.
     */
    sendPeerMessage(message: ZIMMessageBase, toUserID: string, config: ZIMMessageSendConfig): Promise<ZIMMessageSentResult>;
    /**
     * @deprecated Replaced with sendMessage.
     */
    sendGroupMessage(message: ZIMMessageBase, toGroupID: string, config: ZIMMessageSendConfig): Promise<ZIMMessageSentResult>;
    /**
     * @deprecated Replaced with sendMessage.
     */
    sendRoomMessage(message: ZIMMessageBase, toRoomID: string, config: ZIMMessageSendConfig): Promise<ZIMMessageSentResult>;
    sendMediaMessage(message: ZIMMediaMessageBase, toConversationID: string, conversationType: ZIMConversationType, config: ZIMMessageSendConfig, notification?: ZIMMediaMessageSendNotification): Promise<ZIMMediaMessageSentResult>;
    deleteMessages(messageList: ZIMMessage[], conversationID: string, conversationType: ZIMConversationType, config: ZIMMessageDeleteConfig): Promise<ZIMMessageDeletedResult>;
    deleteAllMessage(conversationID: string, conversationType: ZIMConversationType, config: ZIMMessageDeleteConfig): Promise<ZIMMessageDeletedResult>;
    queryHistoryMessage(conversationID: string, conversationType: ZIMConversationType, config: ZIMMessageQueryConfig): Promise<ZIMMessageQueriedResult>;
    insertMessageToLocalDB(message: ZIMMessageBase | ZIMMediaMessageBase, conversationID: string, conversationType: ZIMConversationType, senderUserID: string): Promise<ZIMMessageInsertedResult>;
    downloadMediaFile(message: ZIMMediaMessage, fileType: ZIMMediaFileType, progress: ZIMMediaDownloadingProgress): Promise<ZIMMediaDownloadedResult>;
    createRoom(roomInfo: ZIMRoomInfo, config?: ZIMRoomAdvancedConfig): Promise<ZIMRoomCreatedResult>;
    enterRoom(roomInfo: ZIMRoomInfo, config?: ZIMRoomAdvancedConfig): Promise<ZIMRoomEnteredResult>;
    joinRoom(roomID: string): Promise<ZIMRoomJoinedResult>;
    leaveRoom(roomID: string): Promise<ZIMRoomLeftResult>;
    queryRoomMemberList(roomID: string, config: ZIMRoomMemberQueryConfig): Promise<ZIMRoomMemberQueriedResult>;
    queryRoomOnlineMemberCount(roomID: string): Promise<ZIMRoomOnlineMemberCountQueriedResult>;
    queryRoomAllAttributes(roomID: string): Promise<ZIMRoomAttributesQueriedResult>;
    setRoomAttributes(roomAttributes: Record<string, string>, roomID: string, config: ZIMRoomAttributesSetConfig): Promise<ZIMRoomAttributesOperatedResult>;
    deleteRoomAttributes(keys: string[], roomID: string, config: ZIMRoomAttributesDeleteConfig): Promise<ZIMRoomAttributesOperatedResult>;
    beginRoomAttributesBatchOperation(roomID: string, config: ZIMRoomAttributesBatchOperationConfig): void;
    endRoomAttributesBatchOperation(roomID: string): Promise<ZIMRoomAttributesBatchOperatedResult>;
    setRoomMembersAttributes(attributes: Record<string, string>, userIDs: string[], roomID: string, config: ZIMRoomMemberAttributesSetConfig): Promise<ZIMRoomMembersAttributesOperatedResult>;
    queryRoomMembersAttributes(userIDs: string[], roomID: string): Promise<ZIMRoomMembersAttributesQueriedResult>;
    queryRoomMemberAttributesList(roomID: string, config: ZIMRoomMemberAttributesQueryConfig): Promise<ZIMRoomMemberAttributesListQueriedResult>;
    createGroup(groupInfo: ZIMGroupInfo, userIDs: string[], config?: ZIMGroupAdvancedConfig): Promise<ZIMGroupCreatedResult>;
    joinGroup(groupID: string): Promise<ZIMGroupJoinedResult>;
    leaveGroup(groupID: string): Promise<ZIMGroupLeftResult>;
    dismissGroup(groupID: string): Promise<ZIMGroupDismissedResult>;
    queryGroupList(): Promise<ZIMGroupListQueriedResult>;
    updateGroupNotice(groupNotice: string, groupID: string): Promise<ZIMGroupNoticeUpdatedResult>;
    updateGroupName(groupName: string, groupID: string): Promise<ZIMGroupNameUpdatedResult>;
    updateGroupAvatarUrl(groupAvatarUrl: string, groupID: string): Promise<ZIMGroupAvatarUrlUpdatedResult>;
    queryGroupInfo(groupID: string): Promise<ZIMGroupInfoQueriedResult>;
    setGroupAttributes(groupAttributes: Record<string, string>, groupID: string): Promise<ZIMGroupAttributesOperatedResult>;
    deleteGroupAttributes(keys: string[], groupID: string): Promise<ZIMGroupAttributesOperatedResult>;
    queryGroupAttributes(keys: string[], groupID: string): Promise<ZIMGroupAttributesQueriedResult>;
    queryGroupAllAttributes(groupID: string): Promise<ZIMGroupAttributesQueriedResult>;
    setGroupMemberNickname(nickname: string, forUserID: string, groupID: string): Promise<ZIMGroupMemberNicknameUpdatedResult>;
    setGroupMemberRole(role: number, forUserID: string, groupID: string): Promise<ZIMGroupMemberRoleUpdatedResult>;
    transferGroupOwner(toUserID: string, groupID: string): Promise<ZIMGroupOwnerTransferredResult>;
    queryGroupMemberInfo(userID: string, groupID: string): Promise<ZIMGroupMemberInfoQueriedResult>;
    inviteUsersIntoGroup(userIDs: string[], groupID: string): Promise<ZIMGroupUsersInvitedResult>;
    kickGroupMembers(userIDs: string[], groupID: string): Promise<ZIMGroupMemberKickedResult>;
    queryGroupMemberList(groupID: string, config: ZIMGroupMemberQueryConfig): Promise<ZIMGroupMemberListQueriedResult>;
    queryGroupMemberCount(groupID: string): Promise<ZIMGroupMemberCountQueriedResult>;
    callInvite(invitees: string[], config: ZIMCallInviteConfig): Promise<ZIMCallInvitationSentResult>;
    callCancel(invitees: string[], callID: string, config: ZIMCallCancelConfig): Promise<ZIMCallCancelSentResult>;
    callAccept(callID: string, config: ZIMCallAcceptConfig): Promise<ZIMCallAcceptanceSentResult>;
    callReject(callID: string, config: ZIMCallRejectConfig): Promise<ZIMCallRejectionSentResult>;
}
