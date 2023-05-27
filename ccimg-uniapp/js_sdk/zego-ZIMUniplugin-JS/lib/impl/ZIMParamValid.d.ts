import { ZIMLogger, ZIMLogTag, ZIMLogAction } from './ZIMLogger';
import { ZIMError, ZIMMediaFileType, ZIMUserInfo, ZIMConversationType, ZIMConversationQueryConfig, ZIMCallInviteConfig, ZIMConversationNotificationStatus, ZIMMessageBase, ZIMMessageSendConfig, ZIMMediaMessageBase, ZIMMessage, ZIMMessageDeleteConfig, ZIMMessageQueryConfig, ZIMGroupMessageReceiptMemberQueryConfig, ZIMMediaMessage, ZIMRoomInfo, ZIMRoomAdvancedConfig, ZIMRoomMemberQueryConfig, ZIMRoomMemberAttributesQueryConfig, ZIMGroupInfo, ZIMGroupAdvancedConfig } from '../index';
export declare const NotInitError: ZIMError;
/**
 * Valid API params
 *
 * Used for Web and RN and uni-app
 */
export declare class ZIMParamValid {
    private logger;
    constructor(logger: ZIMLogger);
    validID(field: string, value: string, tag: ZIMLogTag, action: ZIMLogAction): ZIMError | void;
    validName(field: string, value: string, tag: ZIMLogTag, action: ZIMLogAction): ZIMError | void;
    validTwoID(field1: string, value1: string, field2: string, value2: string, tag: ZIMLogTag, action: ZIMLogAction): ZIMError | void;
    validTwoName(field1: string, value1: string, field2: string, value2: string, tag: ZIMLogTag, action: ZIMLogAction): ZIMError | void;
    validIDAndName(field1: string, value1: string, field2: string, value2: string, tag: ZIMLogTag, action: ZIMLogAction): ZIMError | void;
    login(userInfo: ZIMUserInfo): ZIMError | void;
    queryUsersInfo(userIDs: string[]): ZIMError | void;
    validConvIDAndType(conversationID: string, conversationType: ZIMConversationType, action: ZIMLogAction): ZIMError | void;
    queryConversationList(config: ZIMConversationQueryConfig): ZIMError | void;
    setConversationNotificationStatus(status: ZIMConversationNotificationStatus, conversationID: string, conversationType: ZIMConversationType): ZIMError | void;
    sendMessage(message: ZIMMessageBase, toConversationID: string, conversationType: ZIMConversationType, config: ZIMMessageSendConfig): ZIMError | void;
    sendMediaMessage(message: ZIMMediaMessageBase, toConversationID: string, conversationType: ZIMConversationType, config: ZIMMessageSendConfig): ZIMError | void;
    deleteMessages(messageList: ZIMMessage[], conversationID: string, conversationType: ZIMConversationType, config: ZIMMessageDeleteConfig): ZIMError | void;
    queryHistoryMessage(conversationID: string, conversationType: ZIMConversationType, config: ZIMMessageQueryConfig): ZIMError | void;
    downloadMediaFile(message: ZIMMediaMessage, fileType: ZIMMediaFileType): ZIMError | void;
    insertMessageToLocalDB(message: ZIMMessageBase | ZIMMediaMessageBase, conversationID: string, conversationType: ZIMConversationType, senderUserID: string): ZIMError | void;
    sendConversationMessageReceiptRead(conversationID: string, conversationType: ZIMConversationType): ZIMError | void;
    sendMessageReceiptsRead(messageList: ZIMMessage[], conversationID: string, conversationType: ZIMConversationType): ZIMError | void;
    queryMessageReceiptsInfo(messageList: ZIMMessage[], conversationID: string, conversationType: ZIMConversationType): ZIMError | void;
    queryGroupMessageReceiptMemberList(message: ZIMMessage, groupID: string, config: ZIMGroupMessageReceiptMemberQueryConfig, read: boolean, loginUserID: string): ZIMError | void;
    revokeMessage(message: ZIMMessage, loginUserID: string): ZIMError | void;
    createRoom(roomInfo: ZIMRoomInfo, config?: ZIMRoomAdvancedConfig, enter?: boolean): ZIMError | void;
    queryRoomMemberList(roomID: string, config: ZIMRoomMemberQueryConfig): ZIMError | void;
    setRoomAttributes(roomAttributes: Record<string, string>, roomID: string): ZIMError | void;
    deleteRoomAttributes(keys: string[], roomID: string): ZIMError | void;
    setRoomMembersAttributes(attributes: Record<string, string>, userIDs: string[], roomID: string): ZIMError | void;
    queryRoomMembersAttributes(userIDs: string[], roomID: string): ZIMError | void;
    queryRoomMemberAttributesList(roomID: string, config: ZIMRoomMemberAttributesQueryConfig): ZIMError | void;
    validGroupUserIDs(userIDs: string[], groupID: string, action: ZIMLogAction): ZIMError | void;
    createGroup(groupInfo: ZIMGroupInfo, userIDs: string[], config: ZIMGroupAdvancedConfig): ZIMError | void;
    setGroupAttributes(groupAttributes: Record<string, string>, groupID: string): ZIMError | void;
    deleteGroupAttributes(keys: string[], groupID: string): ZIMError | void;
    queryGroupAttributes(groupID: string, keys?: string[]): ZIMError | void;
    setGroupMemberNickname(nickname: string, forUserID: string, groupID: string): ZIMError | void;
    setGroupMemberRole(role: number, forUserID: string, groupID: string): ZIMError | void;
    callInvite(invitees: string[], config: ZIMCallInviteConfig): ZIMError | void;
    callCancel(invitees: string[], callID: string): ZIMError | void;
    /**
     * Message content
     *
     * 1. not empty
     * 2. cannot be a string of all spaces
     * 3. byte <= 48K
     */
    private _checkMesageContent;
    private _checkMesagePriority;
    private _checkMesageType;
    private _checkMediaMesageType;
    private _checkMediaFileType;
    private _checkInsertMesageType;
    private _checkInsertMesageContent;
    private _checkConvType;
    private _checkMessageReceipt;
    private _isValidName;
    private _isValidAttribute;
}
