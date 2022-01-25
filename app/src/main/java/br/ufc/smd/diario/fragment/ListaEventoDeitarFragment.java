package br.ufc.smd.diario.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import br.ufc.smd.diario.R;
import br.ufc.smd.diario.activity.PrincipalActivity;

public class ListaEventoDeitarFragment extends DialogFragment {

    public int valorSelecionado;

    // List<String> selectedItems;
    AlertDialog.Builder builder;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // selectedItems = new ArrayList();  // Where we track the selected items

        final String[] arr = getArguments().getStringArray("eventosDeitar");

        builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle("Fui deitar às...")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setSingleChoiceItems(arr, -1,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // selectedItems.add(arr[which]);
                                valorSelecionado = which;
                            }
                        })
                // Set the action buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the selectedItems results somewhere
                        // or return them to the component that opened the dialog

                        Bundle bundle = new Bundle();
                        bundle.putInt("valorSelecionado", valorSelecionado);

                        Intent intent = new Intent().putExtras(bundle);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                        dismiss();
                    }
                })
                .setNegativeButton("Cancela", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        valorSelecionado = -1;
                        Bundle bundle = new Bundle();
                        bundle.putInt("valorSelecionado", valorSelecionado);

                        Intent intent = new Intent().putExtras(bundle);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                        dismiss();
                    }
                });

        return builder.create();
    }
}
