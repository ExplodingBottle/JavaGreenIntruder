package io.github.explodingbottle.greenintruder.certifier.admintools;

import io.github.explodingbottle.greenintruder.certifier.frame.PageBlockedFrame;

public class BlockedDialogAsyncThread extends Thread {
	private static BlockedDialogAsyncThread firstThread;

	public static void registerFTEnd(BlockedDialogAsyncThread t) {
		if (firstThread == t) {
			firstThread = null;
		}
	}

	public void run() {
		if (firstThread == null) {
			firstThread = this;
			PageBlockedFrame pb = new PageBlockedFrame(this);
			pb.setVisible(true);
		}
	}
}
