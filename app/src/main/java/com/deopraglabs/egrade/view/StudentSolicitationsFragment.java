package com.deopraglabs.egrade.view;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.model.Student;
import com.deopraglabs.egrade.util.DataHolder;
import com.github.chrisbanes.photoview.PhotoView;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class StudentSolicitationsFragment extends Fragment {

    private static final int REQUEST_CODE_SELECT_IMAGE1 = 1;
    private Student student;
    private ImageView imageViewSelected;
    private TextView textViewImageStatus;
    private Bitmap selectedImageBitmap; // This is where you store the selected image bitmap

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        student = DataHolder.getInstance().getStudent();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_solicitation, container, false);

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                view.setBackgroundColor(getResources().getColor(R.color.background_dark));
                break;
            case Configuration.UI_MODE_NIGHT_NO:
            default:
                view.setBackgroundColor(getResources().getColor(android.R.color.background_light));
                break;
        }

        Button buttonViewImage = view.findViewById(R.id.buttonViewImage);
        textViewImageStatus = view.findViewById(R.id.textViewImageStatus);

        buttonViewImage.setOnClickListener(v -> {
            if (selectedImageBitmap != null) {
                showImageDialog(selectedImageBitmap);
            } else {
                Toast.makeText(getContext(), "Nenhuma imagem selecionada", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.buttonSelectImage).setOnClickListener(v -> openGallery(REQUEST_CODE_SELECT_IMAGE1));

        return view;
    }

    private void openGallery(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery(REQUEST_CODE_SELECT_IMAGE1);
            } else {
                Toast.makeText(requireContext(), "Permissão de acesso à galeria negada", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), data.getData());
                selectedImageBitmap = bitmap;
                textViewImageStatus.setText("Imagem Selecionada");
            } catch (Exception e) {
                Toast.makeText(requireActivity(), "Erro ao selecionar imagem", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showImageDialog(Bitmap bitmap) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_image_viewer);
        PhotoView photoView = dialog.findViewById(R.id.photoView);
        photoView.setImageBitmap(bitmap);
        dialog.show();
    }
}
