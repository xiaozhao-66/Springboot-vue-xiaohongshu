export declare enum ZIMConnectionState {
    Disconnected = 0,
    Connecting = 1,
    Connected = 2,
    Reconnecting = 3
}
export declare enum ZIMConnectionEvent {
    Success = 0,
    ActiveLogin = 1,
    LoginTimeout = 2,
    LoginInterrupted = 3,
    KickedOut = 4
}
export declare enum ZIMConversationType {
    Peer = 0,
    Room = 1,
    Group = 2
}
export declare enum ZIMConversationEvent {
    Added = 0,
    Updated = 1,
    Disabled = 2
}
export declare enum ZIMConversationNotificationStatus {
    Notify = 1,
    DoNotDisturb = 2
}
export declare enum ZIMMessagePriority {
    Low = 1,
    Medium = 2,
    High = 3
}
export declare enum ZIMMessageType {
    Unknown = 0,
    Text = 1,
    Command = 2,
    Image = 11,
    File = 12,
    Audio = 13,
    Video = 14,
    Barrage = 20,
    System = 30,
    Revoke = 31
}
export declare enum ZIMMessageSentStatus {
    Sending = 0,
    Success = 1,
    Failed = 2
}
export declare enum ZIMMessageDirection {
    Send = 0,
    Receive = 1
}
export declare enum ZIMMessageReceiptStatus {
    None = 0,
    Processing = 1,
    Done = 2,
    Expired = 3,
    Failed = 4
}
export declare enum ZIMMessageRevokeStatus {
    Unknown = -1,
    SelfRevoke = 0,
    SystemRevoke = 1,
    ServiceAPIRevoke = 2,
    GroupAdminRevoke = 3,
    GroupOwnerRevoke = 4
}
export declare enum ZIMRevokeType {
    TwoWay = 0,
    OneWay = 1
}
export declare enum ZIMMediaFileType {
    OriginalFile = 1,
    LargeImage = 2,
    Thumbnail = 3,
    VideoFirstFrame = 4
}
export declare enum ZIMRoomState {
    Disconnected = 0,
    Connecting = 1,
    Connected = 2
}
export declare enum ZIMRoomEvent {
    Success = 0,
    NetworkInterrupted = 1,
    NetworkDisconnected = 2,
    RoomNotExist = 3,
    ActiveCreate = 4,
    CreateFailed = 5,
    ActiveEnter = 6,
    EnterFailed = 7,
    KickedOut = 8
}
export declare enum ZIMRoomAttributesUpdateAction {
    Set = 0,
    Delete = 1
}
export declare enum ZIMGroupMessageNotificationStatus {
    Notify = 1,
    Disturb = 2
}
export declare enum ZIMGroupState {
    Quit = 0,
    Enter = 1
}
export declare enum ZIMGroupEvent {
    Created = 1,
    Dismissed = 2,
    Joined = 3,
    Invited = 4,
    Left = 5,
    KickedOut = 6
}
export declare enum ZIMGroupMemberState {
    Quit = 0,
    Enter = 1
}
export declare enum ZIMGroupMemberEvent {
    Joined = 1,
    Left = 2,
    KickedOut = 4,
    Invited = 5
}
export declare enum ZIMGroupMemberRole {
    Owner = 1,
    Member = 3
}
export declare enum ZIMGroupAttributesUpdateAction {
    Set = 0,
    Delete = 1
}
export declare enum ZIMCallUserState {
    Inviting = 0,
    Accepted = 1,
    Rejected = 2,
    Cancelled = 3,
    Offline = 4,
    Received = 5
}
export interface ZIMAppConfig {
    appID: number;
    appSign: string;
}
export interface ZIMLogConfig {
    logPath: string;
    logSize: number;
}
export interface ZIMCacheConfig {
    cachePath: string;
}
export interface ZIMUserInfo {
    userID: string;
    userName: string;
}
export interface ZIMUserFullInfo {
    baseInfo: ZIMUserInfo;
    userAvatarUrl: string;
    extendedData: string;
}
export interface ZIMErrorUserInfo {
    userID: string;
    reason: number;
}
export interface ZIMTokenRenewedResult {
    token: string;
}
export interface ZIMUsersInfoQueriedResult {
    userList: ZIMUserFullInfo[];
    errorUserList: ZIMErrorUserInfo[];
}
export interface ZIMUserNameUpdatedResult {
    userName: string;
}
export interface ZIMUserAvatarUrlUpdatedResult {
    userAvatarUrl: string;
}
export interface ZIMUserExtendedDataUpdatedResult {
    extendedData: string;
}
export interface ZIMUsersInfoQueryConfig {
    isQueryFromServer: boolean;
}
export interface ZIMConversation {
    conversationID: string;
    conversationName: string;
    conversationAvatarUrl: string;
    type: ZIMConversationType;
    unreadMessageCount: number;
    orderKey: number;
    notificationStatus: ZIMConversationNotificationStatus;
    lastMessage?: ZIMMessage;
}
export interface ZIMConversationChangeInfo {
    event: ZIMConversationEvent;
    conversation: ZIMConversation;
}
export interface ZIMMessageBase {
    type: ZIMMessageType;
    message: string | Uint8Array;
}
export interface ZIMMediaMessageBase {
    type: ZIMMessageType.Image | ZIMMessageType.File | ZIMMessageType.Audio | ZIMMessageType.Video;
    fileLocalPath: string;
    fileDownloadUrl?: string;
    thumbnailDownloadUrl?: string;
    largeImageDownloadUrl?: string;
    videoFirstFrameDownloadUrl?: string;
    audioDuration?: number;
    videoDuration?: number;
}
export interface ZIMMessage extends ZIMMessageBase {
    localMessageID: string;
    messageID: string;
    senderUserID: string;
    timestamp: number;
    conversationID: string;
    conversationType: ZIMConversationType;
    direction: ZIMMessageDirection;
    sentStatus: ZIMMessageSentStatus;
    orderKey: number;
    conversationSeq: number;
    isUserInserted: boolean;
    receiptStatus: ZIMMessageReceiptStatus;
}
export interface ZIMMediaMessage extends ZIMMediaMessageBase, ZIMMessage {
    type: ZIMMessageType.Image | ZIMMessageType.File | ZIMMessageType.Audio | ZIMMessageType.Video;
    fileLocalPath: string;
    fileDownloadUrl: string;
    fileUID: string;
    fileName: string;
    fileSize: number;
}
export interface ZIMTextMessage extends ZIMMessage {
    type: ZIMMessageType.Text;
    message: string;
}
export interface ZIMCommandMessage extends ZIMMessage {
    type: ZIMMessageType.Command;
    message: Uint8Array;
}
export interface ZIMBarrageMessage extends ZIMMessage {
    type: ZIMMessageType.Barrage;
    message: string;
}
export interface ZIMRevokeMessage extends ZIMMessage {
    type: ZIMMessageType.Revoke;
    message: string;
    revokeType: ZIMRevokeType;
    revokeStatus: ZIMMessageRevokeStatus;
    revokeTimestamp: number;
    operatedUserID: string;
    revokeExtendedData: string;
    originalMessageType: ZIMMessageType;
    originalTextMessageContent: string;
}
export interface ZIMImageMessage extends ZIMMediaMessage {
    type: ZIMMessageType.Image;
    thumbnailDownloadUrl: string;
    largeImageDownloadUrl: string;
    thumbnailLocalPath: string;
    largeImageLocalPath: string;
    originalImageWidth: number;
    originalImageHeight: number;
    thumbnailWidth: number;
    thumbnailHeight: number;
    largeImageWidth: number;
    largeImageHeight: number;
}
export interface ZIMFileMessage extends ZIMMediaMessage {
    type: ZIMMessageType.File;
}
export interface ZIMAudioMessage extends ZIMMediaMessage {
    type: ZIMMessageType.Audio;
    audioDuration: number;
}
export interface ZIMVideoMessage extends ZIMMediaMessage {
    type: ZIMMessageType.Video;
    videoDuration: number;
    videoFirstFrameDownloadUrl: string;
    videoFirstFrameLocalPath: string;
    videoFirstFrameWidth: number;
    videoFirstFrameHeight: number;
}
export interface ZIMMessageReceiptInfo {
    conversationID: string;
    conversationType: ZIMConversationType;
    messageID: string;
    status: ZIMMessageReceiptStatus;
    readMemberCount: number;
    unreadMemberCount: number;
}
export interface ZIMConversationQueryConfig {
    nextConversation?: ZIMConversation;
    count: number;
}
export interface ZIMConversationDeleteConfig {
    isAlsoDeleteServerConversation: boolean;
}
export interface ZIMPushConfig {
    title: string;
    content: string;
    payload: string;
    resourcesID?: string;
}
export interface ZIMMessageSendConfig {
    priority: ZIMMessagePriority;
    hasReceipt?: boolean;
    pushConfig?: ZIMPushConfig;
}
export interface ZIMMessageDeleteConfig {
    isAlsoDeleteServerMessage: boolean;
}
export interface ZIMMessageQueryConfig {
    nextMessage?: ZIMMessage;
    count: number;
    reverse: boolean;
}
export interface ZIMGroupMessageReceiptMemberQueryConfig {
    nextFlag: number;
    count: number;
}
export interface ZIMMessageRevokeConfig {
    revokeExtendedData: string;
    pushConfig?: ZIMPushConfig;
}
export interface ZIMConversationListQueriedResult {
    conversationList: ZIMConversation[];
}
export interface ZIMConversationDeletedResult {
    conversationID: string;
    conversationType: ZIMConversationType;
}
export interface ZIMConversationNotificationStatusSetResult {
    conversationID: string;
    conversationType: ZIMConversationType;
}
export interface ZIMConversationUnreadMessageCountClearedResult {
    conversationID: string;
    conversationType: ZIMConversationType;
}
export interface ZIMMessageDeletedResult {
    conversationID: string;
    conversationType: ZIMConversationType;
}
export interface ZIMMessageSentResult {
    message: ZIMMessage;
}
export interface ZIMMessageQueriedResult {
    conversationID: string;
    conversationType: ZIMConversationType;
    messageList: ZIMMessage[];
}
export interface ZIMMediaMessageSentResult {
    message: ZIMMediaMessage;
}
export declare type ZIMMediaDownloadingProgress = (message: ZIMMediaMessage, currentFileSize: number, totalFileSize: number) => void;
export interface ZIMMediaDownloadedResult {
    message: ZIMMediaMessage;
}
export interface ZIMMessageSendNotification {
    onMessageAttached: (message: ZIMMessage) => void;
}
export interface ZIMMediaMessageSendNotification {
    onMessageAttached: (message: ZIMMediaMessage) => void;
    onMediaUploadingProgress: (message: ZIMMediaMessage, currentFileSize: number, totalFileSize: number) => void;
}
export interface ZIMMessageInsertedResult {
    message: ZIMMessage;
}
export interface ZIMConversationMessageReceiptReadSentResult {
    conversationID: string;
    conversationType: ZIMConversationType;
}
export interface ZIMMessageReceiptsReadSentResult {
    conversationID: string;
    conversationType: ZIMConversationType;
    errorMessageIDs: string[];
}
export interface ZIMMessageReceiptsInfoQueriedResult {
    infos: ZIMMessageReceiptInfo[];
    errorMessageIDs: string[];
}
export interface ZIMGroupMessageReceiptMemberListQueriedResult {
    groupID: string;
    nextFlag: number;
    userList: ZIMGroupMemberInfo[];
}
export interface ZIMMessageRevokedResult {
    message: ZIMRevokeMessage;
}
export interface ZIMRoomMemberQueriedResult {
    roomID: string;
    memberList: ZIMUserInfo[];
    nextFlag: string;
}
export interface ZIMRoomInfo {
    roomID: string;
    roomName: string;
}
export interface ZIMRoomFullInfo {
    baseInfo: ZIMRoomInfo;
}
export interface ZIMRoomAttributesUpdateInfo {
    action: ZIMRoomAttributesUpdateAction;
    roomAttributes: Record<string, string>;
}
export interface ZIMRoomMemberQueryConfig {
    nextFlag: string;
    count: number;
}
export interface ZIMRoomAdvancedConfig {
    roomAttributes: Record<string, string>;
    roomDestroyDelayTime: number;
}
export interface ZIMRoomAttributesDeleteConfig {
    isForce: boolean;
}
export interface ZIMRoomAttributesSetConfig {
    isForce: boolean;
    isUpdateOwner: boolean;
    isDeleteAfterOwnerLeft: boolean;
}
export interface ZIMRoomAttributesBatchOperationConfig {
    isForce: boolean;
    isUpdateOwner: boolean;
    isDeleteAfterOwnerLeft: boolean;
}
export interface ZIMRoomCreatedResult {
    roomInfo: ZIMRoomFullInfo;
}
export interface ZIMRoomEnteredResult {
    roomInfo: ZIMRoomFullInfo;
}
export interface ZIMRoomJoinedResult {
    roomInfo: ZIMRoomFullInfo;
}
export interface ZIMRoomLeftResult {
    roomID: string;
}
export interface ZIMRoomAttributesBatchOperatedResult {
    roomID: string;
}
export interface ZIMRoomOnlineMemberCountQueriedResult {
    roomID: string;
    count: number;
}
export interface ZIMRoomAttributesOperatedResult {
    roomID: string;
    errorKeys: string[];
}
export interface ZIMRoomAttributesQueriedResult {
    roomID: string;
    roomAttributes: Record<string, string>;
}
export interface ZIMRoomMemberAttributesInfo {
    userID: string;
    attributes: Record<string, string>;
}
export interface ZIMRoomMemberAttributesOperatedInfo {
    attributesInfo: ZIMRoomMemberAttributesInfo;
    errorKeys: string[];
}
export interface ZIMRoomMemberAttributesUpdateInfo {
    attributesInfo: ZIMRoomMemberAttributesInfo;
}
export interface ZIMRoomMemberAttributesSetConfig {
    isDeleteAfterOwnerLeft: boolean;
}
export interface ZIMRoomMemberAttributesQueryConfig {
    nextFlag: string;
    count: number;
}
export interface ZIMRoomOperatedInfo {
    userID: string;
}
export interface ZIMRoomMembersAttributesOperatedResult {
    roomID: string;
    infos: ZIMRoomMemberAttributesOperatedInfo[];
    errorUserList: string[];
}
export interface ZIMRoomMembersAttributesQueriedResult {
    roomID: string;
    infos: ZIMRoomMemberAttributesInfo[];
}
export interface ZIMRoomMemberAttributesListQueriedResult {
    roomID: string;
    infos: ZIMRoomMemberAttributesInfo[];
    nextFlag: string;
}
export interface ZIMGroupInfo {
    groupID: string;
    groupName: string;
    groupAvatarUrl: string;
}
export interface ZIMGroupFullInfo {
    baseInfo: ZIMGroupInfo;
    groupNotice: string;
    notificationStatus: ZIMGroupMessageNotificationStatus;
    groupAttributes: Record<string, string>;
}
export interface ZIMGroup {
    baseInfo: ZIMGroupInfo;
    notificationStatus: ZIMGroupMessageNotificationStatus;
}
export interface ZIMGroupMemberInfo extends ZIMUserInfo {
    memberNickname: string;
    memberRole: number;
    memberAvatarUrl: string;
}
export interface ZIMGroupOperatedInfo {
    /**
     * @deprecated
     */
    operatedUserInfo: ZIMGroupMemberInfo;
    userID: string;
    userName: string;
    memberNickname: string;
    memberRole: number;
}
export interface ZIMGroupAttributesUpdateInfo {
    action: ZIMGroupAttributesUpdateAction;
    groupAttributes: Record<string, string>;
}
export interface ZIMGroupAdvancedConfig {
    groupNotice: string;
    groupAttributes: Record<string, string>;
}
export interface ZIMGroupMemberQueryConfig {
    nextFlag: number;
    count: number;
}
export interface ZIMGroupLeftResult {
    groupID: string;
}
export interface ZIMGroupDismissedResult {
    groupID: string;
}
export interface ZIMGroupCreatedResult {
    groupInfo: ZIMGroupFullInfo;
    userList: ZIMGroupMemberInfo[];
    errorUserList: ZIMErrorUserInfo[];
}
export interface ZIMGroupJoinedResult {
    groupInfo: ZIMGroupFullInfo;
}
export interface ZIMGroupInfoQueriedResult {
    groupInfo: ZIMGroupFullInfo;
}
export interface ZIMGroupListQueriedResult {
    groupList: ZIMGroup[];
}
export interface ZIMGroupNameUpdatedResult {
    groupID: string;
    groupName: string;
}
export interface ZIMGroupAvatarUrlUpdatedResult {
    groupID: string;
    groupAvatarUrl: string;
}
export interface ZIMGroupNoticeUpdatedResult {
    groupID: string;
    groupNotice: string;
}
export interface ZIMGroupAttributesOperatedResult {
    groupID: string;
    errorKeys: string[];
}
export interface ZIMGroupAttributesQueriedResult {
    groupID: string;
    groupAttributes: Record<string, string>;
}
export interface ZIMGroupUsersInvitedResult {
    groupID: string;
    userList: ZIMGroupMemberInfo[];
    errorUserList: ZIMErrorUserInfo[];
}
export interface ZIMGroupOwnerTransferredResult {
    groupID: string;
    toUserID: string;
}
export interface ZIMGroupMemberKickedResult {
    groupID: string;
    kickedUserIDs: string[];
    errorUserList: ZIMErrorUserInfo[];
}
export interface ZIMGroupMemberListQueriedResult {
    groupID: string;
    userList: ZIMGroupMemberInfo[];
    nextFlag: number;
}
export interface ZIMGroupMemberInfoQueriedResult {
    groupID: string;
    userInfo: ZIMGroupMemberInfo;
}
export interface ZIMGroupMemberNicknameUpdatedResult {
    groupID: string;
    forUserID: string;
    nickname: string;
}
export interface ZIMGroupMemberRoleUpdatedResult {
    groupID: string;
    forUserID: string;
    role: number;
}
export interface ZIMGroupMemberCountQueriedResult {
    groupID: string;
    count: number;
}
export interface ZIMCallUserInfo {
    userID: string;
    state: ZIMCallUserState;
}
export interface ZIMCallCancelConfig {
    extendedData: string;
}
export interface ZIMCallAcceptConfig {
    extendedData: string;
}
export interface ZIMCallRejectConfig {
    extendedData: string;
}
export interface ZIMCallInviteConfig {
    timeout: number;
    extendedData: string;
    pushConfig?: ZIMPushConfig;
}
export interface ZIMCallAcceptanceSentResult {
    callID: string;
}
export interface ZIMCallRejectionSentResult {
    callID: string;
}
export interface ZIMCallInvitationSentResult {
    callID: string;
    timeout: number;
    errorInvitees: ZIMCallUserInfo[];
}
export interface ZIMCallCancelSentResult {
    callID: string;
    errorInvitees: string[];
}
export interface ZIMMessageReceiptInfo {
    conversationID: string;
    conversationType: ZIMConversationType;
    messageID: string;
    status: ZIMMessageReceiptStatus;
    readMemberCount: number;
    unreadMemberCount: number;
}
export interface ZIMRevokeMessage extends ZIMMessage {
    type: ZIMMessageType.Revoke;
    message: string;
    revokeType: ZIMRevokeType;
    revokeStatus: ZIMMessageRevokeStatus;
    revokeTimestamp: number;
    operatedUserID: string;
    revokeExtendedData: string;
    originalMessageType: ZIMMessageType;
    originalTextMessageContent: string;
}
export interface ZIMMessageSentStatusChangeInfo {
    message: ZIMMessage;
    status: ZIMMessageSentStatus;
}
