export class ZIMLogger {
    warn(tag, action, msg, isShortLog = false) {
        console.log(action, msg && typeof msg != 'string' ? this.stringify(msg, isShortLog) : msg || '');
    }
    /**
     * Format the log to reduce a large number of invalid logs
     *
     * 1. Uint8Array           ->  b=length
     * 2. Array.length > 2     ->  [length, Array[first], Array[last]]
     * 3. String.length > 128  ->  s=length
     *
     */
    stringify(obj, isShortLog = false) {
        const str = JSON.stringify(obj, (key, value) => {
            if (value instanceof Uint8Array)
                return 'b=' + value.length;
            if (value instanceof Array && value.length > 2 && isShortLog) {
                const len = value.length;
                return [len, value[0], value[len - 1]];
            }
            if (typeof value == 'string' && isShortLog && value.length > 128)
                return 's=' + value.length;
            return value;
        });
        return str; //.replace(/\"/g, "'");
    }
}
export var ZIMLogTag;
(function (ZIMLogTag) {
    ZIMLogTag["User"] = "User";
    ZIMLogTag["Conversation"] = "Conversation";
    ZIMLogTag["Room"] = "Room";
    ZIMLogTag["Group"] = "Group";
    ZIMLogTag["Call"] = "Call";
})(ZIMLogTag || (ZIMLogTag = {}));
export var ZIMLogAction;
(function (ZIMLogAction) {
    // API - Main
    ZIMLogAction["Login"] = "JSAPI.login";
    // API - User
    ZIMLogAction["QueryUsersInfo"] = "JSAPI.queryUsersInfo";
    ZIMLogAction["UpdateUserName"] = "JSAPI.updateUserName";
    ZIMLogAction["UpdateUserAvatarUrl"] = "JSAPI.updateUserAvatarUrl";
    ZIMLogAction["UpdateUserExtendedData"] = "JSAPI.updateUserExtendedData";
    // API - Conversation
    ZIMLogAction["QueryConversationList"] = "JSAPI.queryConversationList";
    ZIMLogAction["DeleteConversation"] = "JSAPI.deleteConversation";
    ZIMLogAction["ClearConversationUnreadMessageCount"] = "JSAPI.clearConversationUnreadMessageCount";
    ZIMLogAction["SetConversationNotificationStatus"] = "JSAPI.setConversationNotificationStatus";
    ZIMLogAction["SendMessage"] = "JSAPI.sendMessage";
    ZIMLogAction["SendMediaMessage"] = "JSAPI.sendMediaMessage";
    ZIMLogAction["DeleteMessages"] = "JSAPI.deleteMessages";
    ZIMLogAction["DeleteAllMessage"] = "JSAPI.deleteAllMessage";
    ZIMLogAction["QueryHistoryMessage"] = "JSAPI.queryHistoryMessage";
    ZIMLogAction["DownloadMediaFile"] = "JSAPI.downloadMediaFile";
    ZIMLogAction["InsertMessageToLocalDB"] = "JSAPI.insertMessageToLocalDB";
    ZIMLogAction["SendConversationMessageReceiptRead"] = "JSAPI.sendConversationMessageReceiptRead";
    ZIMLogAction["SendMessageReceiptsRead"] = "JSAPI.sendMessageReceiptsRead";
    ZIMLogAction["QueryMessageReceiptsInfo"] = "JSAPI.queryMessageReceiptsInfo";
    ZIMLogAction["QueryGroupMessageReceiptReadMemberList"] = "JSAPI.queryGroupMessageReceiptReadMemberList";
    ZIMLogAction["QueryGroupMessageReceiptUnreadMemberList"] = "JSAPI.QueryGroupMessageReceiptUnreadMemberList";
    ZIMLogAction["RevokeMessage"] = "JSAPI.RevokeMessage";
    // API - Room
    ZIMLogAction["CreateRoom"] = "JSAPI.createRoom";
    ZIMLogAction["JoinRoom"] = "JSAPI.joinRoom";
    ZIMLogAction["LeaveRoom"] = "JSAPI.leaveRoom";
    ZIMLogAction["QueryRoomMemberList"] = "JSAPI.queryRoomMemberList";
    ZIMLogAction["QueryRoomOnlineMemberCount"] = "JSAPI.queryRoomOnlineMemberCount";
    ZIMLogAction["SetRoomAttributes"] = "JSAPI.setRoomAttributes";
    ZIMLogAction["DeleteRoomAttributes"] = "JSAPI.deleteRoomAttributes";
    ZIMLogAction["QueryRoomAllAttributes"] = "JSAPI.queryRoomAllAttributes";
    ZIMLogAction["BeginRoomAttributesBatchOperation"] = "JSAPI.beginRoomAttributesBatchOperation";
    ZIMLogAction["EndRoomAttributesBatchOperation"] = "JSAPI.endRoomAttributesBatchOperation";
    ZIMLogAction["SetRoomMembersAttributes"] = "JSAPI.setRoomMembersAttributes";
    ZIMLogAction["QueryRoomMembersAttributes"] = "JSAPI.queryRoomMembersAttributes";
    ZIMLogAction["QueryRoomMemberAttributesList"] = "JSAPI.queryRoomMemberAttributesList";
    // API - Group
    ZIMLogAction["CreateGroup"] = "JSAPI.createGroup";
    ZIMLogAction["EnterRoom"] = "JSAPI.enterRoom";
    ZIMLogAction["JoinGroup"] = "JSAPI.joinGroup";
    ZIMLogAction["DismissGroup"] = "JSAPI.dismissGroup";
    ZIMLogAction["LeaveGroup"] = "JSAPI.leaveGroup";
    ZIMLogAction["InviteUsersIntoGroup"] = "JSAPI.inviteUsersIntoGroup";
    ZIMLogAction["KickGroupMembers"] = "JSAPI.kickGroupMembers";
    ZIMLogAction["QueryGroupList"] = "JSAPI.queryGroupList";
    ZIMLogAction["QueryGroupMemberList"] = "JSAPI.queryGroupMemberList";
    ZIMLogAction["QueryGroupMemberCount"] = "JSAPI.queryGroupMemberCount";
    ZIMLogAction["TransferGroupOwner"] = "JSAPI.transferGroupOwner";
    ZIMLogAction["QueryGroupInfo"] = "JSAPI.queryGroupInfo";
    ZIMLogAction["UpdateGroupName"] = "JSAPI.updateGroupName";
    ZIMLogAction["UpdateGroupNotice"] = "JSAPI.updateGroupNotice";
    ZIMLogAction["UpdateGroupAvatarUrl"] = "JSAPI.updateGroupAvatarUrl";
    ZIMLogAction["SetGroupAttributes"] = "JSAPI.setGroupAttributes";
    ZIMLogAction["DeleteGroupAttributes"] = "JSAPI.deleteGroupAttributes";
    ZIMLogAction["QueryGroupAttributes"] = "JSAPI.queryGroupAttributes";
    ZIMLogAction["SetGroupMemberNickname"] = "JSAPI.setGroupMemberNickname";
    ZIMLogAction["SetGroupMemberRole"] = "JSAPI.setGroupMemberRole";
    ZIMLogAction["QueryGroupMemberInfo"] = "JSAPI.queryGroupMemberInfo";
    // API - Call
    ZIMLogAction["CallInvite"] = "JSAPI.callInvite";
    ZIMLogAction["CallCancel"] = "JSAPI.callCancel";
    ZIMLogAction["CallAccept"] = "JSAPI.callAccept";
    ZIMLogAction["CallReject"] = "JSAPI.callReject";
})(ZIMLogAction || (ZIMLogAction = {}));
