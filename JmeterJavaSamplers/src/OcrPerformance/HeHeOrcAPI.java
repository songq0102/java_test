package OcrPerformance;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HeHeOrcAPI{

	public static void main(String[] args) {
		final String ImageSrcFile = "C:/pic/02.jpg";//Recog image path
		final String http_url = "http://100.69.216.49:3308/icr/recognize_hukoubu?owner=1";
//		final String ResultFile = "D:/workspace/Test01/src/OcrPerformance/result.txt";//Result file path
		IdRecognize(ImageSrcFile, http_url);
	}
	
	
	public static int IdRecognize(String imageSrc, String http_url){
		int code = 0;
		try {	
			String filepath = imageSrc;
//			System.out.println(filepath);
			File file = new File(filepath);
			
//			String urlString = "http://10.62.0.252:8088/icr/recognize_id_card?crop_image=1&head_portrait=1&recognize_mode=1";
			String urlString = http_url;
			URL url = new URL(urlString);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestMethod("POST");
			OutputStream out = con.getOutputStream();
			FileInputStream inputStream = new FileInputStream(file);
			byte[] data = new byte[2048];
			int len = 0;
			int sum = 0;
			while ((len = inputStream.read(data))!= -1) {
				out.write(data, 0, len);
				sum = len + sum;
			}
//			System.out.println("upload size="+sum);
			out.flush();
			inputStream.close();
			out.close();
			
			code = con.getResponseCode();		
			System.out.println("code="+code+ " url="+url);
			if (code==200) {
				InputStream inputStream2 = con.getInputStream();
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				while ((len = inputStream2.read(data))!= -1) {
					bos.write(data, 0, len);					
				}
				inputStream2.close();
				String content = new String(bos.toByteArray(),"UTF-8");
				bos.close();
				System.out.println("result ="+content);	
				final String resultFile = System.getProperty("user.dir") + "\\src\\OcrPerformance\\result01.txt";	//Result file path
				FileOutputStream fos = new FileOutputStream(resultFile);
				fos.write(content.getBytes());
				fos.close();
				System.out.println("save result to "+resultFile);
			}
			con.disconnect();	
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return code;
	}
}
