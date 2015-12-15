import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "${clazztablename}")
${pojo.getClassModifiers()} ${pojo.getDeclarationType()} ${pojo.getDeclarationName()} ${pojo.getExtendsDeclaration()} ${pojo.getImplementsDeclaration()}