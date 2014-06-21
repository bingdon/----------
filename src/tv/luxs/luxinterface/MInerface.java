package tv.luxs.luxinterface;

import java.util.List;

import android.util.Log;

import com.dlna.datadefine.DLNA_ConnectionInfo;
import com.dlna.datadefine.DLNA_DeviceData;
import com.dlna.datadefine.DLNA_MediaInfo;
import com.dlna.datadefine.DLNA_PositionInfo;
import com.dlna.datadefine.DLNA_TransportInfo;
import com.dlna.datadefine.DLNA_TransportSettings;
import com.dlna.dmc.UpnpControllerInterface;

public class MInerface implements UpnpControllerInterface{
	@Override
	public void OnUpdateRenderDevice() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnMRStateVariablesChanged(int state, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnSetKeyResult(int res, DLNA_DeviceData device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnSetMouseResult(int res, DLNA_DeviceData device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnSetMessageResult(int res, DLNA_DeviceData device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnGetCurrentTransportActionsResult(int res,
			DLNA_DeviceData device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnGetDeviceCapabilitiesResult(int res, DLNA_DeviceData device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnGetMediaInfoResult(int res, DLNA_DeviceData device,
			DLNA_MediaInfo info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnGetPositionInfoResult(int res, DLNA_DeviceData device,
			DLNA_PositionInfo info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnGetTransportInfoResult(int res, DLNA_DeviceData device,
			DLNA_TransportInfo info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnGetTransportSettingsResult(int res, DLNA_DeviceData device,
			DLNA_TransportSettings settings) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnNextResult(int res, DLNA_DeviceData device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnPauseResult(int res, DLNA_DeviceData device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnPlayResult(int res, DLNA_DeviceData device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnPreviousResult(int res, DLNA_DeviceData device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnSeekResult(int res, DLNA_DeviceData device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnSetAVTransportURIResult(int res, DLNA_DeviceData device) {
		// TODO Auto-generated method stub
		Log.i("�ӿ�", "OnSetAVTransportURIResult:"+res);
	}

	@Override
	public void OnSetPlayModeResult(int res, DLNA_DeviceData device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnStopResult(int res, DLNA_DeviceData device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnGetCurrentConnectionIDsResult(int res,
			DLNA_DeviceData device, String ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnGetCurrentConnectionInfoResult(int res,
			DLNA_DeviceData device, DLNA_ConnectionInfo info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnGetProtocolInfoResult(int res, DLNA_DeviceData device,
			List<String> sources, List<String> sinks) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnSetMuteResult(int res, DLNA_DeviceData device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnGetMuteResult(int res, DLNA_DeviceData device,
			String channel, boolean mute) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnSetVolumeResult(int res, DLNA_DeviceData device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnGetVolumeResult(int res, DLNA_DeviceData device,
			String channel, int volume) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnX_SlideShowResult(int res, DLNA_DeviceData device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnX_FastForwardResult(int res, DLNA_DeviceData device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnX_RewindResult(int res, DLNA_DeviceData device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnUpdateServerDevice() {
		// TODO Auto-generated method stub
		
	}

	
}
