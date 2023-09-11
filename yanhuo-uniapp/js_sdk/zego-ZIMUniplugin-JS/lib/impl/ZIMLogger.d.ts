export declare class ZIMLogger {
    warn(tag: string[], action: string, msg?: string | Record<string, any>, isShortLog?: boolean): void;
    /**
     * Format the log to reduce a large number of invalid logs
     *
     * 1. Uint8Array           ->  b=length
     * 2. Array.length > 2     ->  [length, Array[first], Array[last]]
     * 3. String.length > 128  ->  s=length
     *
     */
    private stringify;
}
export declare enum ZIMLogTag {
    User = "User",
    Conversation = "Conversation",
    Room = "Room",
    Group = "Group",
    Call = "Call"
}
export declare enum ZIMLogAction {
    Login = "JSAPI.login",
    QueryUsersInfo = "JSAPI.queryUsersInfo",
    UpdateUserName = "JSAPI.updateUserName",
    UpdateUserAvatarUrl = "JSAPI.updateUserAvatarUrl",
    UpdateUserExtendedData = "JSAPI.updateUserExtendedData",
    QueryConversationList = "JSAPI.queryConversationList",
    DeleteConversation = "JSAPI.deleteConversation",
    ClearConversationUnreadMessageCount = "JSAPI.clearConversationUnreadMessageCount",
    SetConversationNotificationStatus = "JSAPI.setConversationNotificationStatus",
    SendMessage = "JSAPI.sendMessage",
    SendMediaMessage = "JSAPI.sendMediaMessage",
    DeleteMessages = "JSAPI.deleteMessages",
    DeleteAllMessage = "JSAPI.deleteAllMessage",
    QueryHistoryMessage = "JSAPI.queryHistoryMessage",
    DownloadMediaFile = "JSAPI.downloadMediaFile",
    InsertMessageToLocalDB = "JSAPI.insertMessageToLocalDB",
    SendConversationMessageReceiptRead = "JSAPI.sendConversationMessageReceiptRead",
    SendMessageReceiptsRead = "JSAPI.sendMessageReceiptsRead",
    QueryMessageReceiptsInfo = "JSAPI.queryMessageReceiptsInfo",
    QueryGroupMessageReceiptReadMemberList = "JSAPI.queryGroupMessageReceiptReadMemberList",
    QueryGroupMessageReceiptUnreadMemberList = "JSAPI.QueryGroupMessageReceiptUnreadMemberList",
    RevokeMessage = "JSAPI.RevokeMessage",
    CreateRoom = "JSAPI.createRoom",
    JoinRoom = "JSAPI.joinRoom",
    LeaveRoom = "JSAPI.leaveRoom",
    QueryRoomMemberList = "JSAPI.queryRoomMemberList",
    QueryRoomOnlineMemberCount = "JSAPI.queryRoomOnlineMemberCount",
    SetRoomAttributes = "JSAPI.setRoomAttributes",
    DeleteRoomAttributes = "JSAPI.deleteRoomAttributes",
    QueryRoomAllAttributes = "JSAPI.queryRoomAllAttributes",
    BeginRoomAttributesBatchOperation = "JSAPI.beginRoomAttributesBatchOperation",
    EndRoomAttributesBatchOperation = "JSAPI.endRoomAttributesBatchOperation",
    SetRoomMembersAttributes = "JSAPI.setRoomMembersAttributes",
    QueryRoomMembersAttributes = "JSAPI.queryRoomMembersAttributes",
    QueryRoomMemberAttributesList = "JSAPI.queryRoomMemberAttributesList",
    CreateGroup = "JSAPI.createGroup",
    EnterRoom = "JSAPI.enterRoom",
    JoinGroup = "JSAPI.joinGroup",
    DismissGroup = "JSAPI.dismissGroup",
    LeaveGroup = "JSAPI.leaveGroup",
    InviteUsersIntoGroup = "JSAPI.inviteUsersIntoGroup",
    KickGroupMembers = "JSAPI.kickGroupMembers",
    QueryGroupList = "JSAPI.queryGroupList",
    QueryGroupMemberList = "JSAPI.queryGroupMemberList",
    QueryGroupMemberCount = "JSAPI.queryGroupMemberCount",
    TransferGroupOwner = "JSAPI.transferGroupOwner",
    QueryGroupInfo = "JSAPI.queryGroupInfo",
    UpdateGroupName = "JSAPI.updateGroupName",
    UpdateGroupNotice = "JSAPI.updateGroupNotice",
    UpdateGroupAvatarUrl = "JSAPI.updateGroupAvatarUrl",
    SetGroupAttributes = "JSAPI.setGroupAttributes",
    DeleteGroupAttributes = "JSAPI.deleteGroupAttributes",
    QueryGroupAttributes = "JSAPI.queryGroupAttributes",
    SetGroupMemberNickname = "JSAPI.setGroupMemberNickname",
    SetGroupMemberRole = "JSAPI.setGroupMemberRole",
    QueryGroupMemberInfo = "JSAPI.queryGroupMemberInfo",
    CallInvite = "JSAPI.callInvite",
    CallCancel = "JSAPI.callCancel",
    CallAccept = "JSAPI.callAccept",
    CallReject = "JSAPI.callReject"
}
