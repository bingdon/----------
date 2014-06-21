package tv.luxs.rcassistant;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 登陆界面
 * 
 * @author lyl
 * 
 */
public class LoginActivity extends Activity implements OnClickListener {

	// 邮箱输入框
	private EditText emaliEditText;
	// 密码输入框
	private EditText passwordEditText;
	// 登陆按钮
	private Button loginbtn;
	// 邮箱
	private String Email = "";
	// 密码
	private String password = "";
	// 注册
	private TextView registerTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
	}

	private void initView() {
		emaliEditText = (EditText) findViewById(R.id.email_edit);
		passwordEditText = (EditText) findViewById(R.id.password_edit);
		registerTextView = (TextView) findViewById(R.id.login_register_txt);
		loginbtn = (Button) findViewById(R.id.login_btn);
		loginbtn.setOnClickListener(this);
		registerTextView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.login_btn:
			attemptLogin();
			break;

		case R.id.login_register_txt:

			break;

		default:
			break;
		}
	}

	/**
	 * 登陆
	 */
	private void attemptLogin() {
		emaliEditText.setError(null);
		passwordEditText.setError(null);

		Email = emaliEditText.getText().toString();
		password = passwordEditText.getText().toString();

		if (TextUtils.isEmpty(Email)) {
			emaliEditText.setError(getResources().getString(R.string.editnull));
			return;

		}

		if (!Email.contains("@")) {
			emaliEditText
					.setError(getResources().getString(R.string.emailerro));
			return;
		}

		if (TextUtils.isEmpty(password)) {
			passwordEditText.setError(getResources().getString(
					R.string.passworderro));
			return;
		}

	}

}
