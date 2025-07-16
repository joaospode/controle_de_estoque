package com.example.controledeestoque.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.controledeestoque.model.Funcionario;
import com.example.controledeestoque.model.HistoricoEntrega;
import com.example.controledeestoque.model.Uniforme;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "uniformes.db";
    private static final int VERSION = 1;

    public DatabaseHelper(Context ctx) {
        super(ctx, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabela Funcionarios
        db.execSQL("CREATE TABLE funcionarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT NOT NULL, " +
                "data_nascimento TEXT, " +
                "cpf TEXT UNIQUE, " +
                "setor TEXT, " +
                "funcao TEXT, " +
                "data_admissao TEXT" +
                ");");

        // Tabela Uniformes
        db.execSQL("CREATE TABLE uniformes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tipo TEXT NOT NULL, " +
                "ca TEXT" +
                ");");

        // Histórico de Compras
        db.execSQL("CREATE TABLE compras (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "uniforme_id INTEGER, " +
                "quantidade INTEGER, " +
                "valor_boleto REAL, " +
                "data_entrada TEXT, " +
                "FOREIGN KEY(uniforme_id) REFERENCES uniformes(id)" +
                ");");

        // Estoque (quantidade disponível)
        db.execSQL("CREATE TABLE estoque (" +
                "uniforme_id INTEGER PRIMARY KEY, " +
                "quantidade INTEGER, " +
                "FOREIGN KEY(uniforme_id) REFERENCES uniformes(id)" +
                ");");

        // Histórico de Entregas
        db.execSQL("CREATE TABLE entregas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "funcionario_id INTEGER, " +
                "uniforme_id INTEGER, " +
                "quantidade INTEGER, " +
                "data_entrega TEXT, " +
                "FOREIGN KEY(funcionario_id) REFERENCES funcionarios(id), " +
                "FOREIGN KEY(uniforme_id) REFERENCES uniformes(id)" +
                ");");
    }

    /**
     * Insere um novo funcionário no banco.
     * @return o ID gerado ou -1 em caso de erro.
     */
    public long insertFuncionario(String nome,
                                  String dataNascimento,
                                  String cpf,
                                  String setor,
                                  String funcao,
                                  String dataAdmissao) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nome", nome);
        cv.put("data_nascimento", dataNascimento);
        cv.put("cpf", cpf);
        cv.put("setor", setor);
        cv.put("funcao", funcao);
        cv.put("data_admissao", dataAdmissao);
        return db.insert("funcionarios", null, cv);
    }

    /**
     * Conveniência: insere um funcionário a partir de um objeto.
     */
    public long insertFuncionario(Funcionario f) {
        return insertFuncionario(
                f.getNome(),
                f.getDataNascimento(),
                f.getCpf(),
                f.getSetor(),
                f.getFuncao(),
                f.getDataAdmissao()
        );
    }

    public long insertUniforme(String tipo, String ca) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("tipo", tipo);
        cv.put("ca", ca);
        return db.insert("uniformes", null, cv);
    }

    /** Conveniência: insere a partir de um objeto Uniforme */
    public long insertUniforme(Uniforme u) {
        return insertUniforme(u.getTipo(), String.valueOf(u.getCa()));
    }

    /**
     * Retorna todas as entregas, com dados do funcionário e uniforme, ordenadas por data desc.
     */
    public List<HistoricoEntrega> getAllDeliveries() {
        List<HistoricoEntrega> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT e.id, f.nome AS funcionario, u.tipo AS uniforme, " +
                "e.quantidade, e.data_entrega " +
                "FROM entregas e " +
                "JOIN funcionarios f ON e.funcionario_id = f.id " +
                "JOIN uniformes u ON e.uniforme_id = u.id " +
                "ORDER BY e.data_entrega DESC;";
        Cursor c = db.rawQuery(sql, null);

        if (c.moveToFirst()) {
            do {
                int id = c.getInt(c.getColumnIndexOrThrow("id"));
                String func = c.getString(c.getColumnIndexOrThrow("funcionario"));
                String uni = c.getString(c.getColumnIndexOrThrow("uniforme"));
                int qtde = c.getInt(c.getColumnIndexOrThrow("quantidade"));
                String dataEntrega = c.getString(c.getColumnIndexOrThrow("data_entrega"));
                list.add(new HistoricoEntrega(id, func, uni, qtde, dataEntrega));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public boolean removeStock(int uniformeId, int quantidade) {
        SQLiteDatabase db = getWritableDatabase();
        // Busca quantidade atual
        Cursor c = db.rawQuery(
                "SELECT quantidade FROM estoque WHERE uniforme_id = ?",
                new String[]{String.valueOf(uniformeId)}
        );
        if (!c.moveToFirst()) {
            c.close();
            return false; // uniforme não tem estoque
        }
        int current = c.getInt(0);
        c.close();

        if (current < quantidade) {
            return false; // sem estoque suficiente
        }

        ContentValues cv = new ContentValues();
        cv.put("quantidade", current - quantidade);
        int updated = db.update(
                "estoque",
                cv,
                "uniforme_id = ?",
                new String[]{String.valueOf(uniformeId)}
        );
        return updated > 0;
    }

    /** Remove um funcionário pelo CPF. Retorna número de linhas removidas. */
    public int deleteFuncionarioByCpf(String cpf) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("funcionarios", "cpf=?", new String[]{cpf});
    }

    /** Remove um uniforme pelo tipo. Retorna número de linhas removidas. */
    public int deleteUniformeByTipo(String tipo) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("uniformes", "tipo=?", new String[]{tipo});
    }

    /**
     * Registra uma entrega e atualiza o estoque.
     * @return ID da entrega ou -1 em caso de falha.
     */

    /** Retorna lista de todos os funcionários (id e nome). */
    public List<Funcionario> getAllFuncionarios() {
        List<Funcionario> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id, nome FROM funcionarios ORDER BY nome", null);
        if (c.moveToFirst()) {
            do {
                Funcionario f = new Funcionario();
                f.setId(c.getInt(0));
                f.setNome(c.getString(1));
                list.add(f);
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    /** Retorna lista de todos os uniformes (id e tipo). */
    public List<Uniforme> getAllUniformes() {
        List<Uniforme> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id, tipo FROM uniformes ORDER BY tipo", null);
        if (c.moveToFirst()) {
            do {
                Uniforme u = new Uniforme();
                u.setId(c.getInt(0));
                u.setTipo(c.getString(1));
                list.add(u);
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public boolean addStock(int uniformeId, int quantidade) {
        SQLiteDatabase db = getWritableDatabase();
        // verifica se já existe
        Cursor c = db.rawQuery(
                "SELECT quantidade FROM estoque WHERE uniforme_id = ?",
                new String[]{String.valueOf(uniformeId)}
        );
        int current = 0;
        if (c.moveToFirst()) {
            current = c.getInt(0);
            c.close();
            ContentValues cv = new ContentValues();
            cv.put("quantidade", current + quantidade);
            int rows = db.update(
                    "estoque", cv, "uniforme_id = ?", new String[]{String.valueOf(uniformeId)}
            );
            return rows > 0;
        } else {
            c.close();
            ContentValues cv = new ContentValues();
            cv.put("uniforme_id", uniformeId);
            cv.put("quantidade", quantidade);
            long id = db.insert("estoque", null, cv);
            return id != -1;
        }
    }
    public long insertEntrega(int funcionarioId, int uniformeId, int quantidade, String dataEntrega) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // 1) Insere na tabela entregas
            ContentValues cv = new ContentValues();
            cv.put("funcionario_id", funcionarioId);
            cv.put("uniforme_id", uniformeId);
            cv.put("quantidade", quantidade);
            cv.put("data_entrega", dataEntrega);
            long entregaId = db.insert("entregas", null, cv);
            if (entregaId == -1) throw new RuntimeException("Erro ao inserir entrega");

            // 2) Atualiza estoque (diminui quantidade)
            Cursor c = db.rawQuery(
                    "SELECT quantidade FROM estoque WHERE uniforme_id = ?",
                    new String[]{String.valueOf(uniformeId)}
            );
            int current = 0;
            if (c.moveToFirst()) current = c.getInt(0);
            c.close();
            if (current < quantidade) throw new RuntimeException("Estoque insuficiente");

            ContentValues stock = new ContentValues();
            stock.put("quantidade", current - quantidade);
            int rows = db.update(
                    "estoque", stock, "uniforme_id = ?", new String[]{String.valueOf(uniformeId)}
            );
            if (rows == 0) throw new RuntimeException("Falha ao atualizar estoque");

            db.setTransactionSuccessful();
            return entregaId;
        } catch (Exception ex) {
            return -1;
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS entregas;");
        db.execSQL("DROP TABLE IF EXISTS estoque;");
        db.execSQL("DROP TABLE IF EXISTS compras;");
        db.execSQL("DROP TABLE IF EXISTS uniformes;");
        db.execSQL("DROP TABLE IF EXISTS funcionarios;");
        onCreate(db);
    }
}
