package org.jboss.tools.teiid.reddeer.editor;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import java.util.List;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefFigureCanvas;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefViewer;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCTabItem;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.ui.IEditorReference;
import org.hamcrest.Matcher;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.tools.teiid.reddeer.matcher.IsTransformation;
import org.jboss.tools.teiid.reddeer.matcher.AttributeMatcher;
import org.jboss.tools.teiid.reddeer.matcher.MappingClassMatcher;
import org.jboss.tools.teiid.reddeer.matcher.WaitForFigure;
import org.jboss.tools.teiid.reddeer.matcher.WithBounds;
import org.jboss.tools.teiid.reddeer.matcher.WithLabel;
import org.jboss.tools.teiid.reddeer.widget.SWTBotGefFigure;
import org.jboss.tools.teiid.reddeer.widget.TeiidStyledText;

/**
 * This class represents Model Editor in Teiid Designer perspective.
 * 
 * @author apodhrad
 * 
 */
public class ModelEditor extends SWTBotEditor {

	public static final String TRANSFORMATION_DIAGRAM = "Transformation Diagram";
	public static final String MAPPING_DIAGRAM = "Mapping Diagram";
	public static final String PACKAGE_DIAGRAM = "Package Diagram";
	public static final String TABLE_EDITOR = "Table Editor";
	private static final String DIAGRAM = "Diagram";
	private static final String INPUT_SET = "Input Set";

	private SWTBotGefViewer viewer;

	public ModelEditor(SWTBotEditor editor, SWTWorkbenchBot bot) {
		this(editor.getReference(), bot);
	}

	public ModelEditor(IEditorReference editorReference, SWTWorkbenchBot bot) {
		super(editorReference, bot);
		new SWTWorkbenchBot().sleep(5 * 1000);
	}

	public ModelEditor(String title) {
		super(new SWTWorkbenchBot().editorByTitle(title).getReference(), new SWTWorkbenchBot());
		new SWTWorkbenchBot().sleep(5 * 1000);
	}

	private GraphicalEditor getGraphicalEditor(String tabLabel) {
		final SWTBotCTabItem tabItem = showTab(tabLabel);
		GraphicalEditor graphicalEditor = syncExec(new Result<GraphicalEditor>() {

			@Override
			public GraphicalEditor run() {
				Object obj = tabItem.widget.getData();
				if (obj instanceof GraphicalEditor) {
					return (GraphicalEditor) obj;
				}
				return null;
			}
		});
		return graphicalEditor;
	}

	public SWTBotGefViewer getGraphicalViewer(String tabLabel) {
		final GraphicalEditor graphicalEditor = getGraphicalEditor(tabLabel);
		GraphicalViewer graphicalViewer = syncExec(new Result<GraphicalViewer>() {

			@Override
			public GraphicalViewer run() {
				Object obj = graphicalEditor.getAdapter(GraphicalViewer.class);
				if (obj instanceof GraphicalViewer) {
					return (GraphicalViewer) obj;
				}
				return null;
			}
		});

		return new SWTBotGefViewer(graphicalViewer);
	}

	public SWTBotCTabItem showTab(String label) {
		SWTBotCTabItem tabItem = bot.cTabItem(label);
		tabItem.activate();
		tabItem.show();
		return tabItem;
	}

	public void showTransformation() {
		viewer = getGraphicalViewer(TRANSFORMATION_DIAGRAM);
		viewer.editParts(IsTransformation.isTransformation()).get(0).select();
		viewer.clickContextMenu("Edit");
		new SWTWorkbenchBot().sleep(5 * 1000);
	}

	public void showMappingTransformation(String label) {
		viewer = getGraphicalViewer(MAPPING_DIAGRAM);
		viewer.getEditPart(label).select();
		viewer.clickContextMenu("Edit");
	}

	public CriteriaBuilder criteriaBuilder() {
		bot.toolbarButtonWithTooltip("Criteria Builder").click();
		SWTBotShell shell = bot.shell("Criteria Builder");
		shell.activate();
		return new CriteriaBuilder(shell);
	}

	public void setTransformationProcedureBody(String procedure) {
		String transformationText = getTransformation();
		transformationText = transformationText.replaceAll("<--.*-->;", procedure);

		TeiidStyledText styledText = new TeiidStyledText(0);
		styledText.setText(transformationText);
		styledText.navigateTo(2, procedure.length() / 2);
		styledText.mouseClickOnCaret();
	}
	
	/**
	 * 
	 * @param procedure
	 * @param notReplacingDefault false if editor contains <--.*--> to be replaced, true otherwise 
	 */
	public void setTransformationProcedureBody(String procedure, boolean notReplacingDefault) {
		String transformationText = getTransformation();//""
		if (transformationText.equals("") || (notReplacingDefault)){
			transformationText = procedure;
		} else {
			transformationText = transformationText.replaceAll("<--.*-->;", procedure);
		}
		
		TeiidStyledText styledText = new TeiidStyledText(0);
		styledText.setFocus();
		styledText.setText(transformationText);
		styledText.navigateTo(2, procedure.length() / 2);
		styledText.mouseClickOnCaret();
	}

	public String getTransformation() {
		return bot.styledText(0).getText();
	}

	public void setTransformation(String text) {
		new SWTWorkbenchBot().styledText(0).setText(text);
	}

	public void saveAndValidateSql() {
		clickButtonOnToolbar("Save/Validate SQL");
	}

	public void clickButtonOnToolbar(String button) {
		bot.toolbarButtonWithTooltip(button).click();
	}

	public void showTransformation(String label) {
		SWTBotGefFigure figureBot = figureWithLabel(label);
		editFigure(figureBot);
	}

	public void editFigure(SWTBotGefFigure figureBot) {
		Rectangle rectangle = figureBot.getAbsoluteBounds();
		SWTBotGefFigureCanvas canvas = getFigureCanvas();
		canvas.mouseMoveLeftClick(rectangle.x + 1, rectangle.y + 1);
		canvas.contextMenu("Edit").click();
		bot().waitUntil(new DefaultCondition() {

			@Override
			public boolean test() throws Exception {
				try {
					bot.styledText();
					return true;
				} catch (WidgetNotFoundException wnfe) {
					return false;
				}
			}

			@Override
			public String getFailureMessage() {
				return "Process wasn't completed";
			}
		});
		bot().styledText();
	}

	private SWTBotGefFigureCanvas getFigureCanvas() {
		Matcher<FigureCanvas> matcher = widgetOfType(FigureCanvas.class);
		return new SWTBotGefFigureCanvas((FigureCanvas) bot.widget(matcher, 0));
	}

	public SWTBotGefFigure figureWithLabel(String label) {
		return figureWithLabel(label, 0);
	}

	public SWTBotGefFigure figureWithLabel(String label, int index) {
		Matcher<IFigure> matcher = new WithLabel(label);
		return new SWTBotGefFigure(figure(matcher, index));
	}

	public SWTBotGefFigure tFigure() {
		Matcher matcher = allOf(instanceOf(ImageFigure.class), new WithBounds(40, 60));
		return new SWTBotGefFigure(figure(matcher, 0));
	}

	public IFigure figure(Matcher<IFigure> matcher, int index) {
		SWTBotGefFigureCanvas canvas = getFigureCanvas();
		WaitForFigure waitForFigure = new WaitForFigure(matcher, (FigureCanvas) canvas.widget);
		bot().waitUntil(waitForFigure);
		return waitForFigure.get(index);
	}

	public ModelDiagram getModeDiagram(String label) {
		return getModelDiagram(label, PACKAGE_DIAGRAM);
	}

	public ModelDiagram getModelDiagram(String label, String tab) {
		SWTBotGefViewer viewer = getGraphicalViewer(tab);
		SWTBotGefEditPart editPart = viewer.getEditPart(label);
		if (editPart != null) {
			return new ModelDiagram(editPart);
			//return new ModelDiagram(viewer.getEditPart(label));
		} else {
			return null;
		}
	}
	
	public void selectParts(List<SWTBotGefEditPart> parts){
		if (viewer == null){
			viewer = getGraphicalViewer(MAPPING_DIAGRAM);
		}
		viewer.select(parts);
	}
	
	
	/**
	 * 
	 * @param prefix
	 * @return all attributes (type Label) with name starting with prefix
	 */
	public List<SWTBotGefEditPart> getAttributes(String prefix){
		viewer = getGraphicalViewer(MAPPING_DIAGRAM);
		AttributeMatcher matcher = AttributeMatcher.createAttributeMatcher();
		matcher.setPrefix(prefix);
		return viewer.editParts(matcher); 
	}
	
	
	/**
	 * 
	 * @param prefix
	 * @return all mapping classes (type Label) with name starting with prefix
	 */
	public List<SWTBotGefEditPart> getMappingClasses(String prefix){
		viewer = getGraphicalViewer(MAPPING_DIAGRAM);
		MappingClassMatcher matcher = MappingClassMatcher.createMappingClassMatcher();
		matcher.setPrefix(prefix);
		return viewer.editParts(matcher);
	}
	
	/**
	 * 
	 * @param prefix
	 * @return list of attributes starting with prefix
	 */
	public List<String> namesOfAttributes(String prefix){
			viewer = getGraphicalViewer(MAPPING_DIAGRAM);
			AttributeMatcher matcher = AttributeMatcher.createAttributeMatcher();
			matcher.setPrefix(prefix);
			List<SWTBotGefEditPart> attributes = viewer.editParts(matcher);//generate list of texts
			return matcher.getTexts();
	}
	
	
	public void deleteLabeledItem(String label){
		viewer = getGraphicalViewer(MAPPING_DIAGRAM);
		viewer.select(label);
		viewer.clickContextMenu("Delete");
		log.info("Deleting labeled item " + label);
	}
	
	public Reconciler openReconciler(){
		bot.toolbarButtonWithTooltip("Reconcile Transformation SQL with Target Columns").click();
		SWTBotShell shell = bot.shell("Reconcile Virtual Target Columns");
		shell.activate();
		return new Reconciler(shell);
	}
	
	public void openInputSetEditor(){
		viewer = getGraphicalViewer(DIAGRAM);
		getModelDiagram(INPUT_SET, DIAGRAM).select();
		viewer.clickContextMenu("Edit");
	}
	
}
