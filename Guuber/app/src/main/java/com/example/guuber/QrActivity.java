package com.example.guuber;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

/**
 * This class contains the activity the rider
 * will be directed to upon selecting "generate QR"
 * in their menu
 * it enables their ability to generate a QR
 */

public class QrActivity extends AppCompatActivity {
	private ImageView qrImage;
	private Button qrButton;
	private TextView qrEText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr);

		qrImage = findViewById(R.id.qr_image);
		qrButton = findViewById(R.id.qr_button);
		qrEText = findViewById(R.id.qr_text);

		/**display the back button**/
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		qrButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			String qrString = qrEText.getText().toString();
			/**if no input is provided, prompt user for input**/
			if(TextUtils.isEmpty(qrString)) {
				Toast.makeText(QrActivity.this,"Please enter some text thanks man preciate it", Toast.LENGTH_SHORT).show();
					return;
			}else {
				MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
				try {
					BitMatrix bitMatrix = multiFormatWriter.encode(qrString, BarcodeFormat.QR_CODE, 1000, 1000);
					BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
					Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
					qrImage.setImageBitmap(bitmap);
				} catch (WriterException e) {
					e.printStackTrace(); }
				}
			}
		});
	}

	/**implement logic here for what you want to
	 * happen upon back button press**/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				this.finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}

