package utils.db

import com.github.tminglei.slickpg._

trait PostgresDriver extends ExPostgresProfile with PgDate2Support {
  override val api = new API with DateTimeImplicits with Date2DateTimePlainImplicits
}

object PostgresDriver extends PostgresDriver
