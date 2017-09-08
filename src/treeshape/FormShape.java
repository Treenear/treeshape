package treeshape;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import control.Utils;
import control.color.ColorItemRenderer;
import org.jetbrains.annotations.Nullable;
import view.CountryItemEditor;
import view.CountryItemRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class FormShape extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox cmbColor;
    private JComboBox cmbType;
    private JLabel lblColor;
    private JTextField eNameFile;
    private JButton bColors;
    private JButton btnTest;
    private DefaultComboBoxModel model;
    private VirtualFile dir;
    private Project project;



    public FormShape(@Nullable Project project, VirtualFile dir) {
        this.project = project;
        this.dir = dir;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setResizable(false);
        setLocationRelativeTo(null);

        CreateTypeCmbo();
        initColors(dir);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        String f = eNameFile.getText();
        final String filename = (f.endsWith(".xml") ? f : f + ".xml").trim();
        final String color =  Utils.getColorName(cmbColor);
        final String pressed =  Utils.getColorName(cmbColor);

        if (! Utils.valid(filename, color, pressed)) {
            String title = "Invalidation";
            String msg = "color, pressed, pressedV21 must start with `@color/`";
            Utils.showMessageDialog(project,title, msg);
            return;
        }


        if(eNameFile.getText().isEmpty()||eNameFile.getText()==null){
            Utils.showMessageDialog(project,"Name File", "Name File Can't Empty");
            return;
        }

        if ( Utils.exists(dir,filename)) {
            String title = "Cannot create files";
            String msg = String.format(Locale.US,
                    "`%s` already exists", filename);
            Utils.showMessageDialog(project,title, msg);
            return;
        }


        Application app = ApplicationManager.getApplication();
        app.runWriteAction(new Runnable() {
            @Override
            public void run() {
                try {
//                    createDrawable(filename, color, pressed);
                    String sdp="";
                    switch (cmbType.getSelectedIndex()){
                        case 0:
                            Utils.CreateXmlBackground(dir,project,filename, color, pressed,"100dp");

                            break;

                        case 1:

                            Utils.CreateXmlBackground(dir,project,filename, color, pressed,"0dp");

                            break;

                        case 2:
                            Utils.CreateXmlBackground(dir,project,filename, color, pressed,"10dp");

                            break;
                        case 3:
                            Utils.CreateShape(dir, filename,color, pressed);

                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


    public void CreateUI(@Nullable Project project, VirtualFile dir){
        FormShape dialog = new FormShape(project,dir);
        dialog.pack();
        dialog.setVisible(true);
    }


    private void CreateTypeCmbo(){
        model = new DefaultComboBoxModel();
        for (String[] anItem : Utils.countryList) {
            model.addElement(anItem);
        }
        cmbType.setModel(model);
        cmbType.setRenderer(new CountryItemRenderer());
        cmbType.setEditor(new CountryItemEditor());
        cmbType.setPreferredSize(new Dimension(300, 30));
        cmbType.setEditable(true);



    }


    private boolean initColors(VirtualFile dir) {
        VirtualFile colorsXml = dir.findFileByRelativePath(Utils.valuesColorsXml);
        if (colorsXml != null && colorsXml.exists()) {
            HashMap<String, String> cmap = Utils.parseColorsXml(colorsXml);
            HashMap<String, String> andCmap = Utils.parseAndroidColorsXml(project);

            if (cmap.isEmpty()) {
                String title = "Error";
                String msg = "Cannot find colors in colors.xml";
                Utils.showMessageDialog(project,title, msg);
                return false;
            }

            String regex = "^@(android:)?color/(.+$)";
            ArrayList<String[]> elements = new ArrayList<String[]>();
            for (String name : cmap.keySet()) {
                String color = cmap.get(name);
                while (color != null && color.matches(regex)) {
                    if (color.startsWith("@color/")) {
                        String key = color.replace("@color/", "");
                        color = cmap.get(key);
                    } else if (color.startsWith("@android:color/")) {
                        String key = color.replace("@android:color/", "");
                        color = andCmap.get(key);
                    } else {
                        // not reachable...
                    }
                }

                if (color != null) {
                    elements.add(new String[]{color, name});
                }
            }

            for (Object element : elements) {
                cmbColor.addItem(element);
            }

            ColorItemRenderer renderer = new ColorItemRenderer();
            cmbColor.setRenderer(renderer);

            return !elements.isEmpty();
        }

//        String title = "Error";
//        String msg = String.format("Cannot find %s", Utils.valuesColorsXml);
//        Utils.showMessageDialog(project,title, msg);
        return false;
    }
}
