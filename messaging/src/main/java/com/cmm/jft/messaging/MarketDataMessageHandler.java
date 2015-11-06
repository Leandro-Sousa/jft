package com.cmm.jft.messaging;

import quickfix.FieldNotFound;
import quickfix.IncorrectTagValue;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;
import quickfix.fix44.Heartbeat;
import quickfix.fix44.SequenceReset;
import quickfix.fix50.MarketDataIncrementalRefresh;
import quickfix.fix50.MarketDataSnapshotFullRefresh;
import quickfix.fix50.News;
import quickfix.fix50.SecurityList;
import quickfix.fix50.SecurityStatus;

public interface MarketDataMessageHandler extends MessageHandler {

	void onMessage(SequenceReset message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

	void onMessage(Heartbeat message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

	void onMessage(SecurityList message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

	void onMessage(MarketDataIncrementalRefresh message,
			SessionID sessionID) throws FieldNotFound, UnsupportedMessageType,
			IncorrectTagValue;

	void onMessage(MarketDataSnapshotFullRefresh message,
			SessionID sessionID) throws FieldNotFound, UnsupportedMessageType,
			IncorrectTagValue;

	void onMessage(SecurityStatus message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

	void onMessage(News message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

}