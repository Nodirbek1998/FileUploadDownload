package uz.cas.fileuploaddownload;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import uz.cas.fileuploaddownload.entity.Document;
import uz.cas.fileuploaddownload.repository.DocumentRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FileuploaddownloadApplicationTests {

	@Autowired
	DocumentRepository documentRepository;
	@Autowired
	private TestEntityManager entityManager;
	@Test
	@Rollback(false)
	void testInsertDocument() {
		File file = new File("D:\\yakuniy.doc");
		Document document = new Document();
		document.setName(file.getName());
		try {
			byte[] bytes = Files.readAllBytes(file.toPath());
			long fileSize = bytes.length;
			document.setSize(fileSize);
			document.setContent(bytes);
			document.setUploadTime(new Date());
			Document savedDoc = documentRepository.save(document);
			Document existDoc = entityManager.find(Document.class,savedDoc.getId());

			assertThat(existDoc.getSize()).isEqualTo(fileSize);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
