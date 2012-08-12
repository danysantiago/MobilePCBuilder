/**
MoBu - Mobile PC Builder, and android application about computer hardware.
    Copyright (C) 2011  Daniel Santiago

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/



package com.dany.mobu.builder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dany.mobu.R;

public class ConfigsNameEntry extends Activity {

	protected EditText renameEditBox;
	protected Button renameButton;

	protected Bundle bundle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.textedit_on_dialog);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
				WindowManager.LayoutParams.FLAG_DIM_BEHIND);

		renameButton = (Button) findViewById(R.id.renameButton);
		renameEditBox = (EditText) findViewById(R.id.renameEditBox);

		bundle = this.getIntent().getExtras();

		renameEditBox.setSingleLine();
		renameEditBox.setText(bundle.getString("renameString"));
		renameEditBox.requestFocus();

		// Text Change Listener to verify that before saving there is a valid name
		renameEditBox.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().replaceAll(" ", "").length() > 0)
					renameButton.setEnabled(true);
				else
					renameButton.setEnabled(false);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,int count) {
			}
		});
	}

	public void rename(View view) {
		if (renameEditBox.getText().toString().replaceAll(" ", "").length() > 0) {
			Intent i = new Intent();
			i.putExtra("renameId", bundle.getLong("renameId"));
			i.putExtra("renameString", renameEditBox.getText().toString());
			setResult(RESULT_OK, i);
			finish();
		} else {
			Toast.makeText(this, "Invalid ", Toast.LENGTH_SHORT).show();
		}
	}

}
