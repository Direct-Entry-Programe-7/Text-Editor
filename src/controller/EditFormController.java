package controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.FilterOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class EditFormController {
    public TextArea txtEditor;
    public AnchorPane pneSearch;
    public TextField txtSearch;
    public AnchorPane pneReplace;
    public TextField txtReplace;
    public TextField txtSearch1;

    private int findOffset = -1;
    private List<Index> searchList = new ArrayList<>();


    public void initialize (){
        pneSearch.setVisible(false);
        pneReplace.setVisible(false);

        txtSearch.textProperty().addListener(new TextListener(txtEditor,searchList));
        txtSearch1.textProperty().addListener(new TextListener(txtEditor,searchList));

    }

    public void searchMatches(String query) {
        try {
            Pattern regExp = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
            Matcher matcher = regExp.matcher(txtEditor.getText());

            searchList.clear();

            while (matcher.find()) {
                searchList.add(new EditFormController.Index(matcher.start(), matcher.end()));
            }
            if (searchList.isEmpty()){
                findOffset = -1;
            }
        } catch (PatternSyntaxException e) {
        }

    }

    public void mnuNew_OnAction(ActionEvent actionEvent) {
        txtEditor.clear();
        txtEditor.requestFocus();
    }

    public void mnuExit_OnAction(ActionEvent actionEvent) {
        Platform.exit();

    }

    public void mnuFind_OnAction(ActionEvent actionEvent) {
        if (pneReplace.isVisible()){
            pneReplace.setVisible(false);
        }
        pneSearch.setVisible(true);
        pneSearch.requestFocus();
    }

    public void mnuReplace_OnAction(ActionEvent actionEvent) {
        findOffset = -1;
       if (pneSearch.isVisible()){
           pneSearch.setVisible(false);
       }
       pneReplace.setVisible(true);
       pneReplace.requestFocus();

    }

    public void SelectAll_OnAction(ActionEvent actionEvent) {
        txtEditor.selectAll();
    }

    public void btnSearchNExt_OnAction(ActionEvent actionEvent) {
        if (!searchList.isEmpty()) {
            findOffset++;
            if (findOffset >= searchList.size()) {
                findOffset = 0;
            }
            txtEditor.selectRange(searchList.get(findOffset).startingIndex, searchList.get(findOffset).endIndex);

        }
    }

    public void btnSearchPrevious_OnAction(ActionEvent actionEvent) {
        if (!searchList.isEmpty()) {
            findOffset--;
            if (findOffset < 0) {
                findOffset = searchList.size() - 1;
            }
            }txtEditor.selectRange(searchList.get(findOffset).startingIndex, searchList.get(findOffset).endIndex);

        }


    public void btnReplace_OnAction(ActionEvent actionEvent) {
        if (findOffset == -1) return;
        txtEditor.replaceText(searchList.get(findOffset).startingIndex, searchList.get(findOffset).endIndex, txtReplace.getText());
        searchMatches(txtSearch1.getText());
    }
    public void btnReplaceAll_OnAction(ActionEvent actionEvent) {

        for (int i = 0; i< searchList.size(); i++) {
            txtEditor.replaceText(searchList.get(findOffset).startingIndex, searchList.get(findOffset).endIndex, txtReplace.getText());
            searchMatches(txtSearch1.getText());
            i = -1;
        }
    }

    public void mnuSave_OnAction(ActionEvent actionEvent) {

    }

    static class Index {
    int startingIndex;
    int endIndex;

    public Index(int startingIndex, int endIndex) {
        this.startingIndex = startingIndex;
        this.endIndex = endIndex;
    }
}
}

class TextListener implements ChangeListener<String> {

    private TextArea txtEditor;
    private List<EditFormController.Index> searchList;

    public void searchMatches(String query) {
        try {
            Pattern regExp = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
            Matcher matcher = regExp.matcher(txtEditor.getText());

            searchList.clear();

            while (matcher.find()) {
                searchList.add(new EditFormController.Index(matcher.start(), matcher.end()));
            }

        } catch (PatternSyntaxException e) {
        }
    }
    public TextListener(TextArea txtEditor, List<EditFormController.Index> searchList) {
        this.txtEditor = txtEditor;
        this.searchList = searchList;
    }
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        searchMatches(newValue);
    }


}
















