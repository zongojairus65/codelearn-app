package com.codelearn.api.config;

import com.codelearn.api.model.Chapter;
import com.codelearn.api.model.Exercise;
import com.codelearn.api.model.Lesson;
import com.codelearn.api.repository.ChapterRepository;
import com.codelearn.api.repository.ExerciseRepository;
import com.codelearn.api.repository.LessonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ChapterRepository chapterRepository;
    private final LessonRepository lessonRepository;
    private final ExerciseRepository exerciseRepository;

    public DataInitializer(ChapterRepository chapterRepository,
                            LessonRepository lessonRepository,
                            ExerciseRepository exerciseRepository) {
        this.chapterRepository = chapterRepository;
        this.lessonRepository = lessonRepository;
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public void run(String... args) {
        if (chapterRepository.count() > 0) {
            return;
        }

        Chapter chapter1 = new Chapter();
        chapter1.setTitle("HTML - Les bases");
        chapter1.setDescription("Apprends les fondamentaux du HTML : titres, textes, liens, images, listes, tableaux, formulaires et structure sémantique.");
        chapter1.setOrderIndex(1);
        chapter1.setStatus(Chapter.ChapterStatus.PRELOADED);
        chapterRepository.save(chapter1);

        createLesson1(chapter1);
        createLesson2(chapter1);
        createLesson3(chapter1);
        createLesson4(chapter1);
        createLesson5(chapter1);
    }

    private void createLesson1(Chapter chapter) {
        Lesson lesson = new Lesson();
        lesson.setChapter(chapter);
        lesson.setTitle("Ton premier titre");
        lesson.setTheoryContent("Un titre principal se crée avec la balise <h1>. Elle indique le sujet le plus important de ta page.");
        lesson.setOrderIndex(1);
        lesson.setXpReward(20);
        lessonRepository.save(lesson);

        Exercise ex = new Exercise();
        ex.setLesson(lesson);
        ex.setTitle("Premier titre");
        ex.setInstructions("Ajoute une balise <h1> avec du texte.");
        ex.setStarterHtml("<h1>Salut</h1>");
        ex.setStarterCss("h1 { color: #2563eb; font-family: sans-serif; }");
        ex.setStarterJs("document.querySelector('h1').addEventListener('click', () => alert('Clic !'));");
        ex.setValidationRules("{\"requiredTags\": [\"h1\"], \"minTextLength\": 1}");
        ex.setOrderIndex(1);
        ex.setXpReward(15);
        exerciseRepository.save(ex);
    }

    private void createLesson2(Chapter chapter) {
        Lesson lesson = new Lesson();
        lesson.setChapter(chapter);
        lesson.setTitle("Les niveaux de titres");
        lesson.setTheoryContent("HTML propose 6 niveaux de titres, de <h1> (le plus important) à <h6> (le moins important). On les utilise pour organiser une page comme un plan hiérarchique.");
        lesson.setOrderIndex(2);
        lesson.setXpReward(20);
        lessonRepository.save(lesson);

        Exercise ex = new Exercise();
        ex.setLesson(lesson);
        ex.setTitle("Ajoute un sous-titre");
        ex.setInstructions("Sous le <h1> existant, ajoute un <h2> avec du texte.");
        ex.setStarterHtml("<h1>Mon site</h1>\n<h2>Bienvenue</h2>");
        ex.setStarterCss("h1 { color: #2563eb; } h2 { color: #64748b; }");
        ex.setStarterJs("");
        ex.setValidationRules("{\"requiredTags\": [\"h1\", \"h2\"], \"minTextLength\": 1}");
        ex.setOrderIndex(1);
        ex.setXpReward(15);
        exerciseRepository.save(ex);
    }

    private void createLesson3(Chapter chapter) {
        Lesson lesson = new Lesson();
        lesson.setChapter(chapter);
        lesson.setTitle("Écrire un paragraphe");
        lesson.setTheoryContent("La balise <p> sert à écrire un paragraphe de texte normal, comme dans un article ou un livre.");
        lesson.setOrderIndex(3);
        lesson.setXpReward(20);
        lessonRepository.save(lesson);

        Exercise ex = new Exercise();
        ex.setLesson(lesson);
        ex.setTitle("Ton premier paragraphe");
        ex.setInstructions("Ajoute une balise <p> contenant une phrase.");
        ex.setStarterHtml("<h1>Mon blog</h1>\n<p>Ceci est mon premier article.</p>");
        ex.setStarterCss("body { font-family: sans-serif; }");
        ex.setStarterJs("");
        ex.setValidationRules("{\"requiredTags\": [\"p\"], \"minTextLength\": 5}");
        ex.setOrderIndex(1);
        ex.setXpReward(15);
        exerciseRepository.save(ex);
    }

    private void createLesson4(Chapter chapter) {
        Lesson lesson = new Lesson();
        lesson.setChapter(chapter);
        lesson.setTitle("Mettre en valeur du texte");
        lesson.setTheoryContent("La balise <strong> met un texte en gras pour signaler son importance. La balise <em> met un texte en italique pour l'accentuer.");
        lesson.setOrderIndex(4);
        lesson.setXpReward(20);
        lessonRepository.save(lesson);

        Exercise ex = new Exercise();
        ex.setLesson(lesson);
        ex.setTitle("Mets un mot en valeur");
        ex.setInstructions("Dans le paragraphe, entoure un mot avec <strong> pour le mettre en gras.");
        ex.setStarterHtml("<p>Ce cours est <strong>important</strong> pour progresser.</p>");
        ex.setStarterCss("");
        ex.setStarterJs("");
        ex.setValidationRules("{\"requiredTags\": [\"strong\"], \"minTextLength\": 1}");
        ex.setOrderIndex(1);
        ex.setXpReward(15);
        exerciseRepository.save(ex);
    }

    private void createLesson5(Chapter chapter) {
        Lesson lesson = new Lesson();
        lesson.setChapter(chapter);
        lesson.setTitle("Sauts de ligne et séparateurs");
        lesson.setTheoryContent("La balise <br> insère un saut de ligne à l'intérieur d'un texte. La balise <hr> trace une ligne horizontale pour séparer des sections.");
        lesson.setOrderIndex(5);
        lesson.setXpReward(20);
        lessonRepository.save(lesson);

        Exercise ex = new Exercise();
        ex.setLesson(lesson);
        ex.setTitle("Sépare deux lignes");
        ex.setInstructions("Ajoute un <br> entre les deux lignes de texte suivantes.");
        ex.setStarterHtml("<p>Première ligne<br>Deuxième ligne</p>");
        ex.setStarterCss("");
        ex.setStarterJs("");
        ex.setValidationRules("{\"requiredTags\": [\"br\"], \"minTextLength\": 1}");
        ex.setOrderIndex(1);
        ex.setXpReward(15);
        exerciseRepository.save(ex);
    }
}
