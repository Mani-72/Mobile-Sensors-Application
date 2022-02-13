package com.example.mobilesensorapplication.HeartRateSensorFragment;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mobilesensorapplication.OperationalClasses.Labels;
import com.example.mobilesensorapplication.R;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.WIFI_SERVICE;

public class ServerStartFragment extends Fragment {
    OnSelectedListener callback;
    Button btnStartServer, next;
    TextView txtServerIP;
    EditText edtFileLabel;
    public static String SERVER_IP = "";
    //the msgList is initialized corresponding to the Linearlayout
    private LinearLayout msgList;
    private Handler handler = new Handler();
    boolean startServer = false;

    public ServerStartFragment(OnSelectedListener callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_socket_server, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        //  loadAvailableSensorDate();
        setUpViews(view);
    }

    private void setUpViews(View view) {
        edtFileLabel = (EditText) view.findViewById(R.id.file_label);
        getFileLabel();
        msgList = (LinearLayout) view.findViewById(R.id.msgList);
        txtServerIP = (TextView) view.findViewById(R.id.ip_address);
        getServerIP();
        btnStartServer = (Button) view.findViewById(R.id.start_server);
        next = (Button) view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startServer)
                    callback.proceedSelction("SmartWatch Sensors Data");
                else {
                    Toast toast = Toast.makeText(getContext(), "Please Start Server to Proceed", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        btnStartServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtFileLabel.getText().toString().isEmpty()) {
                    Labels.fileLabel = removeSpaesFileLabel(edtFileLabel.getText().toString());
                    startServer();
                } else {
                    Toast toast = Toast.makeText(getContext(), "Please enter file label to start Server", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    private String removeSpaesFileLabel(String label){
        String temp = label.replaceAll("[\\\\/:*?\"<>|]", "");
        temp= temp.replaceAll("\\s+", "");
        return temp;
    }

    private void startServer() {
        startServer = true;
        callback.startServer();
    }

    private void getFileLabel() {
        if (!Labels.fileLabel.isEmpty()) {
            edtFileLabel.setText(Labels.fileLabel);
        }
    }

    private void getServerIP() {
        try {
            SERVER_IP = getLocalIpAddress();
            txtServerIP.setText("Server IP: " + SERVER_IP);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private String getLocalIpAddress() throws UnknownHostException {
        WifiManager wifiManager = (WifiManager) getActivity().getSystemService(WIFI_SERVICE);
        assert wifiManager != null;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipInt = wifiInfo.getIpAddress();
        return InetAddress.getByAddress(
                ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ipInt).array())
                .getHostAddress();
    }


    public void message(String message) {
        performOperations(message);
        handler.post(new Runnable() {
            @Override
            public void run() {
                TextView tv = new TextView(getContext());
                tv.setText(message + " [" + getTime() + "]");
                msgList.addView(tv);
            }
        });
    }

    private void performOperations(String message) {
        if (message.equals("Phone Server Started.")) {
            btnStartServer.setVisibility(View.GONE);
        }
    }

    //getTime method implemented to format the date into H:m:s
    String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }

}
