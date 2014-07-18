/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.telecomm;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.net.Uri;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;

import com.android.internal.telecomm.IConnectionService;
import com.android.internal.telecomm.IConnectionServiceAdapter;
import com.android.internal.telecomm.ICallVideoProvider;
import com.android.internal.telecomm.RemoteServiceCallback;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Provides methods for IConnectionService implementations to interact with the system phone app.
 *
 * @hide
 */
final class ConnectionServiceAdapter implements DeathRecipient {
    private final Set<IConnectionServiceAdapter> mAdapters = new HashSet<>();

    ConnectionServiceAdapter() {
    }

    void addAdapter(IConnectionServiceAdapter adapter) {
        if (mAdapters.add(adapter)) {
            try {
                adapter.asBinder().linkToDeath(this, 0);
            } catch (RemoteException e) {
                mAdapters.remove(adapter);
            }
        }
    }

    void removeAdapter(IConnectionServiceAdapter adapter) {
        if (mAdapters.remove(adapter)) {
            adapter.asBinder().unlinkToDeath(this, 0);
        }
    }

    /** ${inheritDoc} */
    @Override
    public void binderDied() {
        Iterator<IConnectionServiceAdapter> it = mAdapters.iterator();
        while (it.hasNext()) {
            IConnectionServiceAdapter adapter = it.next();
            if (!adapter.asBinder().isBinderAlive()) {
                it.remove();
                adapter.asBinder().unlinkToDeath(this, 0);
            }
        }
    }

    void handleCreateConnectionSuccessful(ConnectionRequest request) {
        for (IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.handleCreateConnectionSuccessful(request);
            } catch (RemoteException e) {
            }
        }
    }

    void handleCreateConnectionFailed(ConnectionRequest request, int errorCode, String errorMsg) {
        for (IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.handleCreateConnectionFailed(request, errorCode, errorMsg);
            } catch (RemoteException e) {
            }
        }
    }

    void handleCreateConnectionCancelled(ConnectionRequest request) {
        for (IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.handleCreateConnectionCancelled(request);
            } catch (RemoteException e) {
            }
        }
    }

    /**
     * Sets a call's state to active (e.g., an ongoing call where two parties can actively
     * communicate).
     *
     * @param callId The unique ID of the call whose state is changing to active.
     */
    void setActive(String callId) {
        for (IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.setActive(callId);
            } catch (RemoteException e) {
            }
        }
    }

    /**
     * Sets a call's state to ringing (e.g., an inbound ringing call).
     *
     * @param callId The unique ID of the call whose state is changing to ringing.
     */
    void setRinging(String callId) {
        for (IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.setRinging(callId);
            } catch (RemoteException e) {
            }
        }
    }

    /**
     * Sets a call's state to dialing (e.g., dialing an outbound call).
     *
     * @param callId The unique ID of the call whose state is changing to dialing.
     */
    void setDialing(String callId) {
        for (IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.setDialing(callId);
            } catch (RemoteException e) {
            }
        }
    }

    /**
     * Sets a call's state to disconnected.
     *
     * @param callId The unique ID of the call whose state is changing to disconnected.
     * @param disconnectCause The reason for the disconnection, any of
     *            {@link android.telephony.DisconnectCause}.
     * @param disconnectMessage Optional call-service-provided message about the disconnect.
     */
    void setDisconnected(String callId, int disconnectCause, String disconnectMessage) {
        for (IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.setDisconnected(callId, disconnectCause, disconnectMessage);
            } catch (RemoteException e) {
            }
        }
    }

    /**
     * Sets a call's state to be on hold.
     *
     * @param callId - The unique ID of the call whose state is changing to be on hold.
     */
    void setOnHold(String callId) {
        for (IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.setOnHold(callId);
            } catch (RemoteException e) {
            }
        }
    }

    /**
     * Asks Telecomm to start or stop a ringback tone for a call.
     *
     * @param callId The unique ID of the call whose ringback is being changed.
     * @param ringback Whether Telecomm should start playing a ringback tone.
     */
    void setRequestingRingback(String callId, boolean ringback) {
        for (IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.setRequestingRingback(callId, ringback);
            } catch (RemoteException e) {
            }
        }
    }

    void setCallCapabilities(String callId, int capabilities) {
        for (IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.setCallCapabilities(callId, capabilities);
            } catch (RemoteException ignored) {
            }
        }
    }

    /**
     * Indicates whether or not the specified call is currently conferenced into the specified
     * conference call.
     *
     * @param callId The unique ID of the call being conferenced.
     * @param conferenceCallId The unique ID of the conference call. Null if call is not
     *            conferenced.
     */
    void setIsConferenced(String callId, String conferenceCallId) {
        for (IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.setIsConferenced(callId, conferenceCallId);
            } catch (RemoteException ignored) {
            }
        }
    }

    /**
     * Indicates that the call no longer exists. Can be used with either a call or a conference
     * call.
     *
     * @param callId The unique ID of the call.
     */
    void removeCall(String callId) {
        for (IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.removeCall(callId);
            } catch (RemoteException ignored) {
            }
        }
    }

    void onPostDialWait(String callId, String remaining) {
        for (IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.onPostDialWait(callId, remaining);
            } catch (RemoteException ignored) {
            }
        }
    }

    /**
     * Indicates that a new conference call has been created.
     *
     * @param callId The unique ID of the conference call.
     */
    void addConferenceCall(String callId) {
        for (IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.addConferenceCall(callId);
            } catch (RemoteException ignored) {
            }
        }
    }

    /**
     * Retrieves a list of remote connection services usable to place calls.
     */
    void queryRemoteConnectionServices(RemoteServiceCallback callback) {
        // Only supported when there is only one adapter.
        if (mAdapters.size() == 1) {
            try {
                mAdapters.iterator().next().queryRemoteConnectionServices(callback);
            } catch (RemoteException e) {
                Log.e(this, e, "Exception trying to query for remote CSs");
            }
        }
    }

    /**
     * Sets the call video provider for a call.
     *
     * @param callId The unique ID of the call to set with the given call video provider.
     * @param callVideoProvider The call video provider instance to set on the call.
     */
    void setCallVideoProvider(String callId, CallVideoProvider callVideoProvider) {
        for (IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.setCallVideoProvider(callId, callVideoProvider.getInterface());
            } catch (RemoteException e) {
            }
        }
    }

    /**
     * Requests that the framework use VOIP audio mode for this connection.
     *
     * @param callId The unique ID of the call to set with the given call video provider.
     * @param isVoip True if the audio mode is VOIP.
     */
    void setAudioModeIsVoip(String callId, boolean isVoip) {
        for (IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.setAudioModeIsVoip(callId, isVoip);
            } catch (RemoteException e) {
            }
        }
    }

    void setStatusHints(String callId, StatusHints statusHints) {
        for (IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.setStatusHints(callId, statusHints);
            } catch (RemoteException e) {
            }
        }
    }

    void setHandle(String callId, Uri handle, int presentation) {
        for (IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.setHandle(callId, handle, presentation);
            } catch (RemoteException e) {
            }
        }
    }

    void setCallerDisplayName(String callId, String callerDisplayName, int presentation) {
        for (IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.setCallerDisplayName(callId, callerDisplayName, presentation);
            } catch (RemoteException e) {
            }
        }
    }

    /**
     * Sets the video state associated with a call.
     *
     * Valid values: {@link android.telecomm.VideoCallProfile#VIDEO_STATE_AUDIO_ONLY},
     * {@link android.telecomm.VideoCallProfile#VIDEO_STATE_BIDIRECTIONAL},
     * {@link android.telecomm.VideoCallProfile#VIDEO_STATE_TX_ENABLED},
     * {@link android.telecomm.VideoCallProfile#VIDEO_STATE_RX_ENABLED}.
     *
     * @param callId The unique ID of the call to set the video state for.
     * @param videoState The video state.
     */
    void setVideoState(String callId, int videoState) {
        Log.v(this, "setVideoState: %d", videoState);
        for (IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.setVideoState(callId, videoState);
            } catch (RemoteException ignored) {
            }
        }
    }

    void startActivityFromInCall(String callId, PendingIntent intent) {
        for (IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.startActivityFromInCall(callId, intent);
            } catch (RemoteException e) {
            }
        }
    }
}
