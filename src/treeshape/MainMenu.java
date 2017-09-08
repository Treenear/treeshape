package treeshape;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

public class MainMenu extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        super.update(e);
        // TODO: insert action logic here

        final VirtualFile dir = e.getData(LangDataKeys.VIRTUAL_FILE);
        if (dir == null) {
            return;
        }

        Project project = e.getProject();
        FormShape dialog = new FormShape(project, dir);
        dialog.CreateUI(project, dir);

    }
    @Override
    public void update(AnActionEvent e) {
        super.update(e);
        VirtualFile dir = e.getData(LangDataKeys.VIRTUAL_FILE);
        if (dir != null && dir.isDirectory()) {
            String text = dir.getName();
            e.getPresentation().setVisible("res".equals(text));
        }


    }
}
