import ru.gorbunov.vocabulary.backend.repo.tests.*
import ru.gorbunov.vocabulary.repo.common.WordRepoInitialized
import ru.gorbunov.vocabulary.repo.inmemory.WordRepoInMemory

class WordRepoInMemoryCreateTest : RepoWordCreateTest() {
    override val repo = WordRepoInitialized(
        WordRepoInMemory(randomUuid = { uuidNew.asString() }),
        initObjects = initObjects,
    )
}

class WordRepoInMemoryDeleteTest : RepoWordDeleteTest() {
    override val repo = WordRepoInitialized(
        WordRepoInMemory(),
        initObjects = initObjects,
    )
}

class WordRepoInMemoryReadTest : RepoWordReadTest() {
    override val repo = WordRepoInitialized(
        WordRepoInMemory(),
        initObjects = initObjects,
    )
}

class WordRepoInMemorySearchTest : RepoWordSearchTest() {
    override val repo = WordRepoInitialized(
        WordRepoInMemory(),
        initObjects = initObjects,
    )
}

class WordRepoInMemoryUpdateTest : RepoWordUpdateTest() {
    override val repo = WordRepoInitialized(
        WordRepoInMemory(),
        initObjects = initObjects,
    )
}