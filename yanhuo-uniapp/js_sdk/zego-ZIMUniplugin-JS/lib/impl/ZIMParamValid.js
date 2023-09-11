import { ZIMLogTag, ZIMLogAction } from './ZIMLogger';
import { ZIMErrorCode, ZIMMediaFileType, ZIMConversationType, ZIMConversationNotificationStatus, ZIMMessageDirection, ZIMMessageReceiptStatus, ZIMMessageSentStatus, ZIMMessageType, ZIMMessagePriority, } from '../index';
const ZIMConstant = {
    MAX_ID_SIZE: 1024,
    MAX_NAME_SIZE: 2 * 1024,
    MAX_MESSAGE_SIZE: 48 * 1024,
};
const encodeString = (str) => {
    return typeof str == 'string'
        ? // TypedArray.from has problem on som Safari
            new Uint8Array(Array.from(unescape(encodeURIComponent(str || ''))).map((val) => val.charCodeAt(0)))
        : str;
};
const decodeString = (u8arr) => {
    if (typeof u8arr == 'string')
        return u8arr;
    if (!u8arr || !u8arr.length)
        return '';
    const str = String.fromCharCode(...Array.from(u8arr));
    try {
        return decodeURIComponent(escape(str));
    }
    catch (error) {
        return decodeURIComponent(str);
    }
};
const transError = (code, message) => {
    return { code, message };
};
/**
 * byte length <= 1024
 * not start with '#' when create
 */
const isValidID = (str, isCreate) => {
    return (!!str &&
        typeof str == 'string' &&
        str.length <= ZIMConstant.MAX_ID_SIZE &&
        (!isCreate || (isCreate && str[0] != '#')));
};
// export const NotInitError = {
//     code: ZIMErrorCode.CommonModuleNotInit,
//     message: 'The SDK is not initialized.',
// };
/**
 * Valid API params
 *
 * Used for Web and RN and uni-app
 */
export class ZIMParamValid {
    logger;
    constructor(logger) {
        this.logger = logger;
    }
    // MARK: - Common
    validID(field, value, tag, action) {
        if (!isValidID(field)) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, field + ' is invalid.');
            this.logger.warn([tag], action, { error, [field]: value });
            error;
        }
    }
    validName(field, value, tag, action) {
        if (!this._isValidName(value)) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, field + ' is invalid.');
            this.logger.warn([tag], action, { error, [field]: value });
            return error;
        }
    }
    validTwoID(field1, value1, field2, value2, tag, action) {
        let msg = '';
        if ((!isValidID(value1) && (msg = field1 + ' is invalid.')) ||
            (!isValidID(value2) && (msg = field2 + ' is invalid.'))) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            this.logger.warn([tag], action, { error, [field1]: value1, [field2]: value2 });
            return error;
        }
    }
    validTwoName(field1, value1, field2, value2, tag, action) {
        let msg = '';
        if ((!this._isValidName(value1) && (msg = field1 + ' is invalid.')) ||
            (!this._isValidName(value2) && (msg = field2 + ' is invalid.'))) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            this.logger.warn([tag], action, { error, [field1]: value1, [field2]: value2 });
            return error;
        }
    }
    validIDAndName(field1, value1, field2, value2, tag, action) {
        let msg = '';
        if ((!isValidID(value1) && (msg = field1 + ' is invalid.')) ||
            (!this._isValidName(value2) && (msg = field2 + ' is invalid.'))) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            this.logger.warn([tag], action, { error, [field1]: value1, [field2]: value2 });
            return error;
        }
    }
    // MARK: - Main
    login(userInfo) {
        let msg = '';
        if ((!userInfo && (msg = 'userInfo is invalid.')) ||
            (!isValidID(userInfo.userID) && (msg = 'The userInfo instance of userID is invalid.')) ||
            (!this._isValidName(userInfo.userName) && (msg = 'The userInfo instance of userName is invalid.'))) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            this.logger.warn([ZIMLogTag.User], ZIMLogAction.Login, { error, userInfo });
            return error;
        }
    }
    queryUsersInfo(userIDs) {
        if (!Array.isArray(userIDs) || !userIDs.length) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, 'userIDs is invalid.');
            this.logger.warn([ZIMLogTag.User], ZIMLogAction.QueryUsersInfo, { error, userIDs });
            return error;
        }
    }
    // MARK: - Conversation
    validConvIDAndType(conversationID, conversationType, action) {
        let msg = '';
        if ((!isValidID(conversationID) && (msg = 'conversationID is invalid.')) ||
            (conversationType !== ZIMConversationType.Peer &&
                conversationType !== ZIMConversationType.Group &&
                (msg = 'conversationType is invalid.'))) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            this.logger.warn([ZIMLogTag.Conversation], action, { error, conversationID, conversationType });
            return error;
        }
    }
    queryConversationList(config) {
        let msg = '';
        if ((!config && (msg = 'config is invalid.')) ||
            ((typeof config.count != 'number' || config.count < 0) &&
                (msg = 'The config instance of count is invalid.'))) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            this.logger.warn([ZIMLogTag.Conversation], ZIMLogAction.QueryConversationList, { error, config });
            return error;
        }
    }
    setConversationNotificationStatus(status, conversationID, conversationType) {
        let msg = '';
        if ((!isValidID(conversationID) && (msg = 'conversationID is invalid.')) ||
            (conversationType !== ZIMConversationType.Group && (msg = 'conversationType is not support.')) ||
            (status !== ZIMConversationNotificationStatus.Notify &&
                status !== ZIMConversationNotificationStatus.DoNotDisturb &&
                (msg = 'status is invalid.'))) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            const log = { error, conversationID, conversationType, status };
            this.logger.warn([ZIMLogTag.Conversation], ZIMLogAction.SetConversationNotificationStatus, log);
            return error;
        }
    }
    sendMessage(message, toConversationID, conversationType, config) {
        let msg = '';
        if ((!isValidID(toConversationID) && (msg = 'toConversationID is invalid.')) ||
            (!this._checkConvType(conversationType) && (msg = 'conversationType is invalid.')) ||
            (!message && (msg = 'message is invalid.')) ||
            (!config && (msg = 'config is invalid.')) ||
            (!this._checkMesageType(message.type, conversationType) && (msg = 'message type is invalid.')) ||
            (!this._checkMesagePriority(config.priority) && (msg = 'config priority is invalid.')) ||
            (!this._checkMesageContent(conversationType, message.type, message.message) &&
                (msg = 'The message instance of message is invalid.')) ||
            (!this._checkMessageReceipt(conversationType, message.type, config.hasReceipt) &&
                (msg = 'The message type is not support setting receipt.'))) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            const log = { error, toConversationID, conversationType, message, config };
            this.logger.warn([ZIMLogTag.Conversation], ZIMLogAction.SendMessage, log);
            return error;
        }
    }
    sendMediaMessage(message, toConversationID, conversationType, config) {
        let msg = '';
        if ((!isValidID(toConversationID) && (msg = 'toConversationID is invalid.')) ||
            (!this._checkConvType(conversationType) && (msg = 'conversationType is invalid.')) ||
            (!message && (msg = 'message is invalid.')) ||
            (!config && (msg = 'config is invalid.')) ||
            (!this._checkMediaMesageType(message.type) && (msg = 'message type is invalid.')) ||
            (!this._checkMesagePriority(config.priority) && (msg = 'config priority is invalid.')) ||
            (!this._checkMessageReceipt(conversationType, message.type, config.hasReceipt) &&
                (msg = 'The message type is not support setting receipt.')) ||
            (!message.fileLocalPath &&
                !message.fileDownloadUrl &&
                (msg = 'The message instance of fileLocalPath and fileDownloadUrl is empty.'))) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            const log = { error, toConversationID, conversationType, message, config };
            this.logger.warn([ZIMLogTag.Conversation], ZIMLogAction.SendMediaMessage, log);
            return error;
        }
    }
    deleteMessages(messageList, conversationID, conversationType, config) {
        const error = this.validConvIDAndType(conversationID, conversationType, ZIMLogAction.DeleteMessages);
        if (error)
            return error;
        if (!Array.isArray(messageList) || (!messageList.length && config.isAlsoDeleteServerMessage)) {
            this.logger.warn([ZIMLogTag.Conversation], ZIMLogAction.DeleteMessages, { error, messageList, config });
            return transError(ZIMErrorCode.CommonModuleParamInvalid, 'messageList is invalid.');
        }
    }
    queryHistoryMessage(conversationID, conversationType, config) {
        let msg = '';
        if ((!isValidID(conversationID) && (msg = 'conversationID is invalid.')) ||
            (!this._checkConvType(conversationType) && (msg = 'conversationType is invalid.')) ||
            (!config && (msg = 'config is invalid.')) ||
            ((typeof config.count != 'number' || config.count < 0) &&
                (msg = 'The config instance of count is invalid.'))) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            const log = { error, conversationID, conversationType };
            this.logger.warn([ZIMLogTag.Conversation], ZIMLogAction.QueryHistoryMessage, log);
            return error;
        }
    }
    downloadMediaFile(message, fileType) {
        let msg = '';
        if ((!message && (msg = 'message is invalid.')) ||
            (!message.fileDownloadUrl && (msg = 'The message instance of fileDownloadUrl is empty.')) ||
            (!this._checkMediaMesageType(message.type) && (msg = 'message type is invalid.')) ||
            (!this._checkMediaFileType(fileType) && (msg = 'fileType is invalid.'))) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            const log = { error, fileType, message };
            this.logger.warn([ZIMLogTag.Conversation], ZIMLogAction.DownloadMediaFile, log);
            return error;
        }
    }
    insertMessageToLocalDB(message, conversationID, conversationType, senderUserID) {
        const error = this.validConvIDAndType(conversationID, conversationType, ZIMLogAction.InsertMessageToLocalDB);
        if (error)
            return error;
        let msg = '';
        if ((!isValidID(senderUserID) && (msg = 'senderUserID is invalid.')) ||
            (!message && (msg = 'message is invalid.')) ||
            (!this._checkInsertMesageType(message.type) && (msg = 'message type is invalid.')) ||
            (!this._checkInsertMesageContent(message) && (msg = 'The message instance of message is invalid.'))) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            const log = { error, conversationID, conversationType, senderUserID, message };
            this.logger.warn([ZIMLogTag.Conversation], ZIMLogAction.InsertMessageToLocalDB, log);
            return error;
        }
    }
    sendConversationMessageReceiptRead(conversationID, conversationType) {
        let msg = '';
        if ((!isValidID(conversationID) && (msg = 'conversationID is invalid.')) ||
            (conversationType !== ZIMConversationType.Peer && (msg = 'conversationType is invalid.'))) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            const log = { error, conversationID, conversationType };
            this.logger.warn([ZIMLogTag.Conversation], ZIMLogAction.SendConversationMessageReceiptRead, log);
            return error;
        }
    }
    sendMessageReceiptsRead(messageList, conversationID, conversationType) {
        const error = this.validConvIDAndType(conversationID, conversationType, ZIMLogAction.SendMessageReceiptsRead);
        if (error)
            return error;
        let msg = '';
        if (!Array.isArray(messageList) || messageList.length == 0) {
            msg = 'messageList is invalid.';
        }
        else {
            const flag = messageList.some((item) => {
                return (item.conversationID == conversationID &&
                    item.conversationType == conversationType &&
                    item.direction == ZIMMessageDirection.Receive &&
                    item.receiptStatus == ZIMMessageReceiptStatus.Processing);
            });
            if (!flag) {
                msg = 'Every one of message list is not a received receipt message in the conversationID.';
            }
        }
        if (msg) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            const log = { error, conversationID, conversationType, messageList };
            this.logger.warn([ZIMLogTag.Conversation], ZIMLogAction.SendMessageReceiptsRead, log, true);
            return error;
        }
    }
    queryMessageReceiptsInfo(messageList, conversationID, conversationType) {
        const error = this.validConvIDAndType(conversationID, conversationType, ZIMLogAction.QueryMessageReceiptsInfo);
        if (error)
            return error;
        let msg = '';
        if (!Array.isArray(messageList) || messageList.length == 0) {
            msg = 'messageList is invalid.';
        }
        else {
            const flag = messageList.every((item) => {
                return (!item.receiptStatus ||
                    item.conversationID !== conversationID ||
                    item.conversationType !== conversationType);
            });
            if (flag) {
                msg = 'Every one of message list is not a receipt message in the conversationID.';
            }
        }
        if (msg) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            const log = { error, conversationID, conversationType, messageList };
            this.logger.warn([ZIMLogTag.Conversation], ZIMLogAction.QueryMessageReceiptsInfo, log, true);
            return error;
        }
    }
    queryGroupMessageReceiptMemberList(message, groupID, config, read, loginUserID) {
        let msg = '';
        if ((!isValidID(groupID) && (msg = 'groupID is invalid.')) ||
            ((!config || typeof config.count != 'number' || config.count < 0) &&
                (msg = 'config or the config instance of count is invalid.')) ||
            (!message && (msg = 'message is invalid.')) ||
            ((message.senderUserID != loginUserID ||
                message.sentStatus != ZIMMessageSentStatus.Success ||
                !message.receiptStatus) &&
                (msg = 'The message instance is not a successful receipt message sent by self.')) ||
            ((message.type == ZIMMessageType.Command || message.type == ZIMMessageType.Revoke) &&
                (msg = 'The message type is not support.')) ||
            ((message.conversationID !== groupID || message.conversationType !== ZIMConversationType.Group) &&
                (msg = 'The message instance is not belong the groupID.'))) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            const action = read
                ? ZIMLogAction.QueryGroupMessageReceiptReadMemberList
                : ZIMLogAction.QueryGroupMessageReceiptUnreadMemberList;
            this.logger.warn([ZIMLogTag.Group], action, { error, groupID, message, config });
            return error;
        }
    }
    revokeMessage(message, loginUserID) {
        let msg = '';
        if ((!message && (msg = 'message is invalid.')) ||
            ((message.isUserInserted ||
                message.senderUserID != loginUserID ||
                message.sentStatus != ZIMMessageSentStatus.Success) &&
                (msg = 'The message instance is not a send successful message.')) ||
            (message.conversationType !== ZIMConversationType.Peer &&
                message.conversationType !== ZIMConversationType.Group &&
                (msg = 'Only peer and group message can be revoked.'))) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            this.logger.warn([ZIMLogTag.Conversation], ZIMLogAction.RevokeMessage, { error, message });
            return error;
        }
    }
    // MARK: - Room
    createRoom(roomInfo, config, enter) {
        let msg = '';
        if ((!roomInfo && (msg = 'roomInfo is invalid.')) ||
            (!isValidID(roomInfo.roomID) && (msg = 'The roomInfo instance of roomID is invalid.')) ||
            (!this._isValidName(roomInfo.roomName) && (msg = 'The roomInfo instance of roomName is invalid.')) ||
            (config &&
                config.roomAttributes &&
                !this._isValidAttribute(config.roomAttributes, true) &&
                (msg =
                    'The Key and Value of the roomAttributes property of the config instance only support string types.'))) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            const action = enter ? ZIMLogAction.EnterRoom : ZIMLogAction.CreateRoom;
            this.logger.warn([ZIMLogTag.Room], action, { error, roomInfo, config });
            return error;
        }
    }
    queryRoomMemberList(roomID, config) {
        let msg = '';
        if ((!isValidID(roomID) && (msg = 'roomID is invalid.')) ||
            ((!config || typeof config.count != 'number' || config.count < 0) &&
                (msg = 'config or the config instance of count is invalid.'))) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            this.logger.warn([ZIMLogTag.Room], ZIMLogAction.QueryRoomMemberList, { error, roomID, config });
            return error;
        }
    }
    setRoomAttributes(roomAttributes, roomID) {
        let msg = '';
        if ((!isValidID(roomID) && (msg = 'roomID is invalid.')) ||
            (!this._isValidAttribute(roomAttributes) &&
                (msg = 'The Key and Value of the roomAttributes only support string types.'))) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            this.logger.warn([ZIMLogTag.Room], ZIMLogAction.SetRoomAttributes, { error, roomID, roomAttributes });
            return error;
        }
    }
    deleteRoomAttributes(keys, roomID) {
        let msg = '';
        if ((!isValidID(roomID) && (msg = 'roomID is invalid.')) ||
            ((!Array.isArray(keys) || !keys.length || keys.some((item) => !item || typeof item !== 'string')) &&
                (msg = 'Every one of keys must be a valid string type.'))) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            this.logger.warn([ZIMLogTag.Room], ZIMLogAction.DeleteRoomAttributes, { error, roomID, keys });
            return error;
        }
    }
    setRoomMembersAttributes(attributes, userIDs, roomID) {
        let msg = '';
        if ((!isValidID(roomID) && (msg = 'roomID is invalid.')) ||
            (!this._isValidAttribute(attributes) &&
                (msg = 'The Key and Value of the attributes only support string types.')) ||
            ((!Array.isArray(userIDs) ||
                !userIDs.length ||
                userIDs.some((item) => !item || typeof item !== 'string')) &&
                (msg = 'Every one of userIDs must be a valid string type.'))) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            this.logger.warn([ZIMLogTag.Room], ZIMLogAction.SetRoomMembersAttributes, { error, roomID, attributes });
            return error;
        }
    }
    queryRoomMembersAttributes(userIDs, roomID) {
        let msg = '';
        if ((!isValidID(roomID) && (msg = 'roomID is invalid.')) ||
            ((!Array.isArray(userIDs) ||
                !userIDs.length ||
                userIDs.some((item) => !item || typeof item !== 'string')) &&
                (msg = 'Every one of userIDs must be a valid string type.'))) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            this.logger.warn([ZIMLogTag.Room], ZIMLogAction.QueryRoomMembersAttributes, { error, roomID, userIDs });
            return error;
        }
    }
    queryRoomMemberAttributesList(roomID, config) {
        let msg = '';
        if ((!isValidID(roomID) && (msg = 'roomID is invalid.')) ||
            ((!config || typeof config.count != 'number' || config.count < 0) &&
                (msg = 'config or the config instance of count is invalid.'))) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            this.logger.warn([ZIMLogTag.Room], ZIMLogAction.QueryRoomMemberAttributesList, { error, roomID, config });
            return error;
        }
    }
    // MARK: - Group
    validGroupUserIDs(userIDs, groupID, action) {
        let msg = '';
        if (((!Array.isArray(userIDs) ||
            !userIDs.length ||
            userIDs.some((item) => !item || typeof item !== 'string')) &&
            (msg = 'Every one of userIDs must be a valid string type.')) ||
            (!isValidID(groupID) && (msg = 'groupID is invalid.'))) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            this.logger.warn([ZIMLogTag.Group], action, { error, groupID, userIDs });
            return error;
        }
    }
    createGroup(groupInfo, userIDs, config) {
        const groupID = String(groupInfo.groupID || '').trim();
        let msg = '';
        const paramInvalid = (groupID && !isValidID(groupID, true) && (msg = 'The groupInfo instance of groupID is invalid.')) ||
            (!this._isValidName(groupInfo.groupName) && (msg = 'The groupInfo instance of groupName is invalid.')) ||
            (!this._isValidName(groupInfo.groupAvatarUrl) &&
                (msg = 'The groupInfo instance of groupAvatarUrl is invalid.')) ||
            (!this._isValidName(config.groupNotice) && (msg = 'The config instance of groupNotice is invalid.')) ||
            (userIDs && !Array.isArray(userIDs) && (msg = 'Every one of userIDs must be a valid string type.')) ||
            (!this._isValidAttribute(config.groupAttributes, true) &&
                (msg =
                    'The Key and Value of the groupAttributes property of the config instance only support string types.'));
        if (paramInvalid) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            this.logger.warn([ZIMLogTag.Group], ZIMLogAction.CreateGroup, { error, groupInfo, config, userIDs });
            return error;
        }
    }
    setGroupAttributes(groupAttributes, groupID) {
        let msg = '';
        if ((!isValidID(groupID) && (msg = 'groupID is invalid.')) ||
            (!this._isValidAttribute(groupAttributes) &&
                (msg = 'The Key and Value of the groupAttributes only support string types.'))) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            this.logger.warn([ZIMLogTag.Group], ZIMLogAction.SetGroupAttributes, { error, groupID, groupAttributes });
            return error;
        }
    }
    deleteGroupAttributes(keys, groupID) {
        let msg = '';
        if ((!isValidID(groupID) && (msg = 'groupID is invalid.')) ||
            ((!Array.isArray(keys) || !keys.length || keys.some((item) => !item || typeof item !== 'string')) &&
                (msg = 'Every one of kyes must be a valid string type.'))) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            this.logger.warn([ZIMLogTag.Group], ZIMLogAction.DeleteGroupAttributes, { error, groupID, keys });
            return error;
        }
    }
    queryGroupAttributes(groupID, keys) {
        let msg = '';
        if ((!isValidID(groupID) && (msg = 'groupID is invalid.')) ||
            (Array.isArray(keys) &&
                (!keys.length || keys.some((item) => !item || typeof item !== 'string')) &&
                (msg = 'Every one of kyes must be a valid string type.'))) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            this.logger.warn([ZIMLogTag.Group], ZIMLogAction.QueryGroupAttributes, { error, groupID, keys });
            return error;
        }
    }
    setGroupMemberNickname(nickname, forUserID, groupID) {
        let msg = '';
        if ((!this._isValidName(nickname) && (msg = 'nickname is invalid.')) ||
            (!isValidID(forUserID) && (msg = 'forUserID is invalid.')) ||
            (!isValidID(groupID) && (msg = 'groupID is invalid.'))) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            const log = { error, groupID, forUserID, nickname };
            this.logger.warn([ZIMLogTag.Group], ZIMLogAction.SetGroupMemberNickname, log);
            return error;
        }
    }
    setGroupMemberRole(role, forUserID, groupID) {
        let msg = '';
        if ((typeof role !== 'number' && (msg = 'role is invalid.')) ||
            (!isValidID(forUserID) && (msg = 'forUserID is invalid.')) ||
            (!isValidID(groupID) && (msg = 'groupID is invalid.'))) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            this.logger.warn([ZIMLogTag.Group], ZIMLogAction.SetGroupMemberRole, { error, groupID, forUserID, role });
            return error;
        }
    }
    // MARK: - Call
    callInvite(invitees, config) {
        let msg = '';
        if (((!Array.isArray(invitees) || !invitees.length) && (msg = 'invitees is invalid.')) ||
            (config &&
                typeof config.timeout == 'number' &&
                (config.timeout % 1 != 0 || config.timeout < 1 || config.timeout > 600) &&
                (msg = 'The config instance of timeout is invalid.'))) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            this.logger.warn([ZIMLogTag.Call], ZIMLogAction.CallInvite, { error, invitees, config });
            return error;
        }
    }
    callCancel(invitees, callID) {
        let msg = '';
        if ((!isValidID(callID) && (msg = 'callID is invalid.')) ||
            ((!Array.isArray(invitees) || !invitees.length) && (msg = 'invitees is invalid.'))) {
            const error = transError(ZIMErrorCode.CommonModuleParamInvalid, msg);
            this.logger.warn([ZIMLogTag.Call], ZIMLogAction.CallCancel, { error, callID, invitees });
            return error;
        }
    }
    // MARK: - Private help function
    /**
     * Message content
     *
     * 1. not empty
     * 2. cannot be a string of all spaces
     * 3. byte <= 48K
     */
    _checkMesageContent(convType, msgType, content) {
        if ((msgType === ZIMMessageType.Text ||
            (msgType === ZIMMessageType.Barrage && convType === ZIMConversationType.Room)) &&
            (typeof content !== 'string' ||
                !content ||
                !content.trim() ||
                encodeString(content).length > ZIMConstant.MAX_MESSAGE_SIZE)) {
            return false;
        }
        if (msgType === ZIMMessageType.Command &&
            (!(content instanceof Uint8Array) ||
                !content.length ||
                content.length > ZIMConstant.MAX_MESSAGE_SIZE ||
                !decodeString(content).trim())) {
            return false;
        }
        return true;
    }
    _checkMesagePriority(priority) {
        return (priority === ZIMMessagePriority.Low ||
            priority === ZIMMessagePriority.Medium ||
            priority === ZIMMessagePriority.High);
    }
    _checkMesageType(msgType, convType) {
        return (msgType === ZIMMessageType.Text ||
            msgType === ZIMMessageType.Command ||
            (convType === ZIMConversationType.Room && msgType === ZIMMessageType.Barrage));
    }
    _checkMediaMesageType(msgType) {
        return (msgType === ZIMMessageType.Image ||
            msgType === ZIMMessageType.File ||
            msgType === ZIMMessageType.Audio ||
            msgType === ZIMMessageType.Video);
    }
    _checkMediaFileType(fileType) {
        return (fileType === ZIMMediaFileType.OriginalFile ||
            fileType === ZIMMediaFileType.LargeImage ||
            fileType === ZIMMediaFileType.Thumbnail ||
            fileType === ZIMMediaFileType.VideoFirstFrame);
    }
    _checkInsertMesageType(msgType) {
        return (msgType === ZIMMessageType.Text ||
            msgType === ZIMMessageType.System ||
            msgType === ZIMMessageType.Image ||
            msgType === ZIMMessageType.File ||
            msgType === ZIMMessageType.Audio ||
            msgType === ZIMMessageType.Video);
    }
    _checkInsertMesageContent(msg) {
        const content = msg.message;
        if ((msg.type === ZIMMessageType.Text || msg.type === ZIMMessageType.System) &&
            (typeof content !== 'string' ||
                !content ||
                !content.trim() ||
                encodeString(content).length > ZIMConstant.MAX_MESSAGE_SIZE)) {
            return false;
        }
        const _msg = msg;
        if (msg.type >= ZIMMessageType.Image &&
            msg.type <= ZIMMessageType.Video &&
            !_msg.fileLocalPath &&
            !_msg.fileDownloadUrl) {
            return false;
        }
        return true;
    }
    _checkConvType(type) {
        return (type === ZIMConversationType.Peer || type === ZIMConversationType.Room || type === ZIMConversationType.Group);
    }
    // Room message unsupport receipt
    _checkMessageReceipt(type, msgType, hasReceipt) {
        if (!hasReceipt ||
            (type != ZIMConversationType.Room && msgType != ZIMMessageType.Command && msgType != ZIMMessageType.Barrage)) {
            return true;
        }
        return false;
    }
    // byte length <= 2048
    _isValidName = (str) => {
        return typeof str == 'string' && str.length <= ZIMConstant.MAX_NAME_SIZE;
    };
    // Key,Value must be string
    _isValidAttribute = (attr, canEmpty) => {
        if (!attr || Object.prototype.toString.call(attr) !== '[object Object]')
            return false;
        const keys = Object.keys(attr);
        if (!canEmpty && !keys.length)
            return false;
        return Object.keys(attr).every((key) => typeof key == 'string' && typeof attr[key] == 'string');
    };
}
