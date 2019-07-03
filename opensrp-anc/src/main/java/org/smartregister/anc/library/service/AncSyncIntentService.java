package org.smartregister.anc.library.service;

import org.smartregister.anc.library.AncLibrary;
import org.smartregister.sync.ClientProcessorForJava;
import org.smartregister.sync.intent.SyncIntentService;

/**
 * Created by ndegwamartin on 11/02/2019.
 */
public class AncSyncIntentService extends SyncIntentService {

    @Override
    protected ClientProcessorForJava getClientProcessor() {
        return AncLibrary.getInstance().getClientProcessorForJava();
    }
}
