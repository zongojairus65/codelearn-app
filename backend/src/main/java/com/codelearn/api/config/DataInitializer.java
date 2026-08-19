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
            return; // déjà initialisé, on ne recrée rien
        }

        Chapter chapter1 = new Chapter();
        chapter1.setTitle("HTML - Les bases");
        chapter1.setDescription("Apprends les fondamentaux du HTML : titres, textes, liens, images, listes, tableaux, formulaires et structure sémantique.");
        chapter1.setOrderIndex(1);
        chapter1.setStatus(Chapter.ChapterStatus.PRELOADED);
        chapterRepository.save(chapter1);

        Lesson lesson1 = new Lesson();
        lesson1.setChapter(chapter1);
        lesson1.setTitle("Ton premier titre");
        lesson1.setTheoryContent("Un titre principal se crée avec la balise <h1>. Elle indique le sujet le plus important de ta page.");
        lesson1.setOrderIndex(1);
        lesson1.setXpReward(20);
        lessonRepository.save(lesson1);

        Exercise ex1 = new Exercise();
        ex1.setLesson(lesson1);
        ex1.setTitle("Premier titre");
        ex1.setInstructions("Ajoute une balise <h1> avec du texte.");
        ex1.setStarterHtml("<h1>Salut</h1>");
        ex1.setStarterCss("h1 { color: #2563eb; font-family: sans-serif; }");
        ex1.setStarterJs("document.querySelector('h1').addEventListener('click', () => alert('Clic !'));");
        ex1.setValidationRules("{\"requiredTags\": [\"h1\"], \"minTextLength\": 1}");
        ex1.setOrderIndex(1);
        ex1.setXpReward(15);
        exerciseRepository.save(ex1);
    }
}
