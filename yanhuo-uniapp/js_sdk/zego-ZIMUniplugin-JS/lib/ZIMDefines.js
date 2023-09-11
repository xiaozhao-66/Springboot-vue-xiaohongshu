// MARK: - enum
export var ZIMConnectionState;
(function (ZIMConnectionState) {
    ZIMConnectionState[ZIMConnectionState["Disconnected"] = 0] = "Disconnected";
    ZIMConnectionState[ZIMConnectionState["Connecting"] = 1] = "Connecting";
    ZIMConnectionState[ZIMConnectionState["Connected"] = 2] = "Connected";
    ZIMConnectionState[ZIMConnectionState["Reconnecting"] = 3] = "Reconnecting";
})(ZIMConnectionState || (ZIMConnectionState = {}));
export var ZIMConnectionEvent;
(function (ZIMConnectionEvent) {
    ZIMConnectionEvent[ZIMConnectionEvent["Success"] = 0] = "Success";
    ZIMConnectionEvent[ZIMConnectionEvent["ActiveLogin"] = 1] = "ActiveLogin";
    ZIMConnectionEvent[ZIMConnectionEvent["LoginTimeout"] = 2] = "LoginTimeout";
    ZIMConnectionEvent[ZIMConnectionEvent["LoginInterrupted"] = 3] = "LoginInterrupted";
    ZIMConnectionEvent[ZIMConnectionEvent["KickedOut"] = 4] = "KickedOut";
})(ZIMConnectionEvent || (ZIMConnectionEvent = {}));
export var ZIMConversationType;
(function (ZIMConversationType) {
    ZIMConversationType[ZIMConversationType["Peer"] = 0] = "Peer";
    ZIMConversationType[ZIMConversationType["Room"] = 1] = "Room";
    ZIMConversationType[ZIMConversationType["Group"] = 2] = "Group";
})(ZIMConversationType || (ZIMConversationType = {}));
export var ZIMConversationEvent;
(function (ZIMConversationEvent) {
    ZIMConversationEvent[ZIMConversationEvent["Added"] = 0] = "Added";
    ZIMConversationEvent[ZIMConversationEvent["Updated"] = 1] = "Updated";
    ZIMConversationEvent[ZIMConversationEvent["Disabled"] = 2] = "Disabled";
})(ZIMConversationEvent || (ZIMConversationEvent = {}));
export var ZIMConversationNotificationStatus;
(function (ZIMConversationNotificationStatus) {
    ZIMConversationNotificationStatus[ZIMConversationNotificationStatus["Notify"] = 1] = "Notify";
    ZIMConversationNotificationStatus[ZIMConversationNotificationStatus["DoNotDisturb"] = 2] = "DoNotDisturb";
})(ZIMConversationNotificationStatus || (ZIMConversationNotificationStatus = {}));
export var ZIMMessagePriority;
(function (ZIMMessagePriority) {
    ZIMMessagePriority[ZIMMessagePriority["Low"] = 1] = "Low";
    ZIMMessagePriority[ZIMMessagePriority["Medium"] = 2] = "Medium";
    ZIMMessagePriority[ZIMMessagePriority["High"] = 3] = "High";
})(ZIMMessagePriority || (ZIMMessagePriority = {}));
export var ZIMMessageType;
(function (ZIMMessageType) {
    ZIMMessageType[ZIMMessageType["Unknown"] = 0] = "Unknown";
    ZIMMessageType[ZIMMessageType["Text"] = 1] = "Text";
    ZIMMessageType[ZIMMessageType["Command"] = 2] = "Command";
    ZIMMessageType[ZIMMessageType["Image"] = 11] = "Image";
    ZIMMessageType[ZIMMessageType["File"] = 12] = "File";
    ZIMMessageType[ZIMMessageType["Audio"] = 13] = "Audio";
    ZIMMessageType[ZIMMessageType["Video"] = 14] = "Video";
    ZIMMessageType[ZIMMessageType["Barrage"] = 20] = "Barrage";
    ZIMMessageType[ZIMMessageType["System"] = 30] = "System";
    ZIMMessageType[ZIMMessageType["Revoke"] = 31] = "Revoke";
})(ZIMMessageType || (ZIMMessageType = {}));
export var ZIMMessageSentStatus;
(function (ZIMMessageSentStatus) {
    ZIMMessageSentStatus[ZIMMessageSentStatus["Sending"] = 0] = "Sending";
    ZIMMessageSentStatus[ZIMMessageSentStatus["Success"] = 1] = "Success";
    ZIMMessageSentStatus[ZIMMessageSentStatus["Failed"] = 2] = "Failed";
})(ZIMMessageSentStatus || (ZIMMessageSentStatus = {}));
export var ZIMMessageDirection;
(function (ZIMMessageDirection) {
    ZIMMessageDirection[ZIMMessageDirection["Send"] = 0] = "Send";
    ZIMMessageDirection[ZIMMessageDirection["Receive"] = 1] = "Receive";
})(ZIMMessageDirection || (ZIMMessageDirection = {}));
export var ZIMMessageReceiptStatus;
(function (ZIMMessageReceiptStatus) {
    ZIMMessageReceiptStatus[ZIMMessageReceiptStatus["None"] = 0] = "None";
    ZIMMessageReceiptStatus[ZIMMessageReceiptStatus["Processing"] = 1] = "Processing";
    ZIMMessageReceiptStatus[ZIMMessageReceiptStatus["Done"] = 2] = "Done";
    ZIMMessageReceiptStatus[ZIMMessageReceiptStatus["Expired"] = 3] = "Expired";
    ZIMMessageReceiptStatus[ZIMMessageReceiptStatus["Failed"] = 4] = "Failed";
})(ZIMMessageReceiptStatus || (ZIMMessageReceiptStatus = {}));
export var ZIMMessageRevokeStatus;
(function (ZIMMessageRevokeStatus) {
    ZIMMessageRevokeStatus[ZIMMessageRevokeStatus["Unknown"] = -1] = "Unknown";
    ZIMMessageRevokeStatus[ZIMMessageRevokeStatus["SelfRevoke"] = 0] = "SelfRevoke";
    ZIMMessageRevokeStatus[ZIMMessageRevokeStatus["SystemRevoke"] = 1] = "SystemRevoke";
    ZIMMessageRevokeStatus[ZIMMessageRevokeStatus["ServiceAPIRevoke"] = 2] = "ServiceAPIRevoke";
    ZIMMessageRevokeStatus[ZIMMessageRevokeStatus["GroupAdminRevoke"] = 3] = "GroupAdminRevoke";
    ZIMMessageRevokeStatus[ZIMMessageRevokeStatus["GroupOwnerRevoke"] = 4] = "GroupOwnerRevoke";
})(ZIMMessageRevokeStatus || (ZIMMessageRevokeStatus = {}));
export var ZIMRevokeType;
(function (ZIMRevokeType) {
    ZIMRevokeType[ZIMRevokeType["TwoWay"] = 0] = "TwoWay";
    ZIMRevokeType[ZIMRevokeType["OneWay"] = 1] = "OneWay";
})(ZIMRevokeType || (ZIMRevokeType = {}));
export var ZIMMediaFileType;
(function (ZIMMediaFileType) {
    ZIMMediaFileType[ZIMMediaFileType["OriginalFile"] = 1] = "OriginalFile";
    ZIMMediaFileType[ZIMMediaFileType["LargeImage"] = 2] = "LargeImage";
    ZIMMediaFileType[ZIMMediaFileType["Thumbnail"] = 3] = "Thumbnail";
    ZIMMediaFileType[ZIMMediaFileType["VideoFirstFrame"] = 4] = "VideoFirstFrame";
})(ZIMMediaFileType || (ZIMMediaFileType = {}));
export var ZIMRoomState;
(function (ZIMRoomState) {
    ZIMRoomState[ZIMRoomState["Disconnected"] = 0] = "Disconnected";
    ZIMRoomState[ZIMRoomState["Connecting"] = 1] = "Connecting";
    ZIMRoomState[ZIMRoomState["Connected"] = 2] = "Connected";
})(ZIMRoomState || (ZIMRoomState = {}));
export var ZIMRoomEvent;
(function (ZIMRoomEvent) {
    ZIMRoomEvent[ZIMRoomEvent["Success"] = 0] = "Success";
    ZIMRoomEvent[ZIMRoomEvent["NetworkInterrupted"] = 1] = "NetworkInterrupted";
    ZIMRoomEvent[ZIMRoomEvent["NetworkDisconnected"] = 2] = "NetworkDisconnected";
    ZIMRoomEvent[ZIMRoomEvent["RoomNotExist"] = 3] = "RoomNotExist";
    ZIMRoomEvent[ZIMRoomEvent["ActiveCreate"] = 4] = "ActiveCreate";
    ZIMRoomEvent[ZIMRoomEvent["CreateFailed"] = 5] = "CreateFailed";
    ZIMRoomEvent[ZIMRoomEvent["ActiveEnter"] = 6] = "ActiveEnter";
    ZIMRoomEvent[ZIMRoomEvent["EnterFailed"] = 7] = "EnterFailed";
    ZIMRoomEvent[ZIMRoomEvent["KickedOut"] = 8] = "KickedOut";
})(ZIMRoomEvent || (ZIMRoomEvent = {}));
export var ZIMRoomAttributesUpdateAction;
(function (ZIMRoomAttributesUpdateAction) {
    ZIMRoomAttributesUpdateAction[ZIMRoomAttributesUpdateAction["Set"] = 0] = "Set";
    ZIMRoomAttributesUpdateAction[ZIMRoomAttributesUpdateAction["Delete"] = 1] = "Delete";
})(ZIMRoomAttributesUpdateAction || (ZIMRoomAttributesUpdateAction = {}));
export var ZIMGroupMessageNotificationStatus;
(function (ZIMGroupMessageNotificationStatus) {
    ZIMGroupMessageNotificationStatus[ZIMGroupMessageNotificationStatus["Notify"] = 1] = "Notify";
    ZIMGroupMessageNotificationStatus[ZIMGroupMessageNotificationStatus["Disturb"] = 2] = "Disturb";
})(ZIMGroupMessageNotificationStatus || (ZIMGroupMessageNotificationStatus = {}));
export var ZIMGroupState;
(function (ZIMGroupState) {
    ZIMGroupState[ZIMGroupState["Quit"] = 0] = "Quit";
    ZIMGroupState[ZIMGroupState["Enter"] = 1] = "Enter";
})(ZIMGroupState || (ZIMGroupState = {}));
export var ZIMGroupEvent;
(function (ZIMGroupEvent) {
    ZIMGroupEvent[ZIMGroupEvent["Created"] = 1] = "Created";
    ZIMGroupEvent[ZIMGroupEvent["Dismissed"] = 2] = "Dismissed";
    ZIMGroupEvent[ZIMGroupEvent["Joined"] = 3] = "Joined";
    ZIMGroupEvent[ZIMGroupEvent["Invited"] = 4] = "Invited";
    ZIMGroupEvent[ZIMGroupEvent["Left"] = 5] = "Left";
    ZIMGroupEvent[ZIMGroupEvent["KickedOut"] = 6] = "KickedOut";
})(ZIMGroupEvent || (ZIMGroupEvent = {}));
export var ZIMGroupMemberState;
(function (ZIMGroupMemberState) {
    ZIMGroupMemberState[ZIMGroupMemberState["Quit"] = 0] = "Quit";
    ZIMGroupMemberState[ZIMGroupMemberState["Enter"] = 1] = "Enter";
})(ZIMGroupMemberState || (ZIMGroupMemberState = {}));
export var ZIMGroupMemberEvent;
(function (ZIMGroupMemberEvent) {
    ZIMGroupMemberEvent[ZIMGroupMemberEvent["Joined"] = 1] = "Joined";
    ZIMGroupMemberEvent[ZIMGroupMemberEvent["Left"] = 2] = "Left";
    ZIMGroupMemberEvent[ZIMGroupMemberEvent["KickedOut"] = 4] = "KickedOut";
    ZIMGroupMemberEvent[ZIMGroupMemberEvent["Invited"] = 5] = "Invited";
})(ZIMGroupMemberEvent || (ZIMGroupMemberEvent = {}));
export var ZIMGroupMemberRole;
(function (ZIMGroupMemberRole) {
    ZIMGroupMemberRole[ZIMGroupMemberRole["Owner"] = 1] = "Owner";
    ZIMGroupMemberRole[ZIMGroupMemberRole["Member"] = 3] = "Member";
})(ZIMGroupMemberRole || (ZIMGroupMemberRole = {}));
export var ZIMGroupAttributesUpdateAction;
(function (ZIMGroupAttributesUpdateAction) {
    ZIMGroupAttributesUpdateAction[ZIMGroupAttributesUpdateAction["Set"] = 0] = "Set";
    ZIMGroupAttributesUpdateAction[ZIMGroupAttributesUpdateAction["Delete"] = 1] = "Delete";
})(ZIMGroupAttributesUpdateAction || (ZIMGroupAttributesUpdateAction = {}));
export var ZIMCallUserState;
(function (ZIMCallUserState) {
    ZIMCallUserState[ZIMCallUserState["Inviting"] = 0] = "Inviting";
    ZIMCallUserState[ZIMCallUserState["Accepted"] = 1] = "Accepted";
    ZIMCallUserState[ZIMCallUserState["Rejected"] = 2] = "Rejected";
    ZIMCallUserState[ZIMCallUserState["Cancelled"] = 3] = "Cancelled";
    ZIMCallUserState[ZIMCallUserState["Offline"] = 4] = "Offline";
    ZIMCallUserState[ZIMCallUserState["Received"] = 5] = "Received";
})(ZIMCallUserState || (ZIMCallUserState = {}));
